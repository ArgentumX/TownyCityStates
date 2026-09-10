# AGENTS.md

This file provides guidance to agent when working with code in this repository.

## Overview

TownyCityStates is a PaperMC (Minecraft 1.21, Java 21) plugin that adds "city states"
on top of the **Towny** and **WorldGuard** plugins. A city state ties a WorldGuard
region to a Towny overlord (a town) and grants gameplay bonuses to that town's
residents. Both Towny and WorldGuard are hard dependencies (see `plugin.yml`).

## MAIN RULES
1) YOU ARE WORKING IN READ-ONLY MODE - YOU CAN GUIDE OR RECOMMEND SOMETHING CHANGES TO USER. YOU CAN EDIT ONLY IF USER DIRECTLY ALLOW THAT.

## Build & Run & Test

Use the Gradle wrapper (`./gradlew`); the project uses the **shadow** plugin and
**run-paper** for a local test server.

- `./gradlew build` — compiles and produces the shaded plugin jar (`build` depends on `shadowJar`). `shadowJar` runs `minimize()` and relocates Guice to `com.argemtum.shadow.guice` to avoid classpath conflicts.
- `./gradlew loadPlugins` — one-time setup for the run server: copies the Towny jar into `run/plugins/` and downloads WorldEdit + WorldGuard jars (skips if present).
- `./gradlew runServer` — launches a real Paper 1.21 test server with the built plugin (depends on `shadowJar`). Run `loadPlugins` first so Towny/WG are present. The server world and configs live under `run/`.

`.mcp.json` configures a `minecraft-bot` MCP server that connects to a running
server on `localhost:25565` as `ClaudeBot` — useful for driving the test server.

project has 3 test types: unit/integration/e2e (with mineflayer scripts). Prefer ability to run them parallel.

## Architecture

Clean/onion architecture wired together with **Guice** DI. Everything is constructed
in `TownyCityStates.onEnable()` via `Guice.createInjector(new PluginModule(this))`;
`PluginModule` (`di/`) is the single place bindings are declared. To add a service,
use case, repository, adapter, or command, bind it there. Repositories and adapters
are `asEagerSingleton()`.

Layers (outer depends on inner; inner depends only on `abstraction/` interfaces):

- **commands/** — ACF (`co.aikar.commands`) `BaseCommand` classes. `tcs_admin`/`tcsa` (admin, `tcs.admin`) and `tcs`/`townycitystates` (user, `tcs.user`). Commands are thin: they resolve localized messages and delegate to use cases / services. Registered in `TownyCityStates.registerCommands()` via `PaperCommandManager`, which also wires the `@city-states` async tab-completion.
- **controllers/** — application logic. `usecases/` are single-responsibility operations (create city, reward, reload, info); `services/` (`CityStateService`) coordinate broader flows.
- **repositories/** — persistence + in-memory cache. `CityStateYamlRepository` loads/saves each city as its own YAML file under `<dataFolder>/cityStates/<name>.yml`, keyed by `UUID` in memory; saves run **async** via the Bukkit scheduler. Config and localization have their own repositories. Access through the `abstraction/` interfaces (`ICityStateRepository`, `IConfigRepository`, `ILocalizationRepository`).
- **adapters/** — thin wrappers over external plugin APIs: `TownyAdapter` (Towny) and `WorldGuardAdapter` (WorldGuard region create/query). Keep all direct Towny/WorldGuard API calls behind these.
- **objects/** — the domain model (see below).
- **config/** — typed config + localization nodes (`ConfigNode`, `MessageNode`, `Localization`). User-facing strings come from `resources/languages/*.yml`; add a `MessageNode` and look it up with `localization.of(node, args...)` rather than hardcoding text.

### Domain model: CityState + behaviors

`objects/city/CityState` is the aggregate root. It does **not** contain logic itself;
it composes three pluggable **behaviors** (`objects/city/behaviors/`), each exposed
through a narrow interface:

- `CityInfluenceBehavior` → `IInfluenceable`
- `CityBonusBehavior` → `IBonusProvider`
- `CityOverlordBehavior` → `IOverlordable`

Persistence uses **Configurate** object mapping: `CityState` and the behaviors are
`@ConfigSerializable` with `@Setting` fields and a **private no-arg constructor** for
deserialization. After a `CityState` is loaded from YAML, `init()` must be called to
re-bind each behavior back to its parent (the repository does this in
`loadFromFile`). When adding serialized state, keep the no-arg ctor + `init()` pattern
intact or Configurate round-tripping breaks.

### Bonus / event system

Bonuses are `CityBonus` subclasses (`objects/bonus/`, e.g. `HealthBonus`). A bonus
declares its effects in `registerHandlers()` by calling
`registerHandler(EventClass, consumer, EventSource)`, mapping a Bukkit event type to
a handler, scoped to an `EventSource` (`PLAYER` or `TOWN`). `BonusRegistry`
(`listeners/`) flattens all bonuses of all cities into a
`UUID -> (EventClass -> handlers)` map (`registerAllHandlers()`), so an incoming event
for a given town/resident UUID can be dispatched via `handleEvent(event, uuid)`.

Note: `BonusRegistry.registerAllHandlers()`/`handleEvent()` are not yet invoked from a
registered Bukkit `Listener` — the dispatch glue is in progress (recent "bonus
tracking" commits). When wiring it up, register the plugin as a Bukkit listener and
route events through `handleEvent`.

## Conventions

- Follow the layer boundaries: depend on the `abstraction/` interfaces, not concrete classes, and add new bindings in `PluginModule`.
- Constructor injection with `@com.google.inject.Inject`; no field injection.
- Keep external plugin (Towny/WorldGuard/WorldEdit) API usage inside `adapters/`.
- User-facing text goes through `Localization`/`MessageNode`, backed by `resources/languages/`.
