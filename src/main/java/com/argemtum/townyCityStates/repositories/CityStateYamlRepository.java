package com.argemtum.townyCityStates.repositories;

import com.argemtum.townyCityStates.TownyCityStates;
import com.argemtum.townyCityStates.objects.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.google.inject.Inject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CityStateYamlRepository implements ICityStateRepository {
    private final Path cityStatesDir;
    private final Map<UUID, CityState> cityStates = new HashMap<>();
    private final Yaml yaml;
    private final TownyCityStates plugin;

    @Inject
    public CityStateYamlRepository(TownyCityStates plugin) {
        this.plugin = plugin;
        this.cityStatesDir = plugin.getDataFolder().toPath().resolve("cityStates");
        this.yaml = createYaml();
    }

    private Yaml createYaml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(options);
    }

    @Override
    public void loadAll() {
        cityStates.clear();
        try {
            Files.createDirectories(cityStatesDir);
            try (Stream<Path> files = Files.list(cityStatesDir)) {
                files.filter(path -> path.toString().endsWith(".yml"))
                        .forEach(this::loadFromFile);
            }
            plugin.getLogger().info(String.format("Loaded %d cities.", cityStates.size()));
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load city states: " + e.getMessage());
        }
    }

    private void loadFromFile(Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            Map<String, Object> data = yaml.load(reader);
            CityState city = CityState.fromMap(data);
            cityStates.put(city.getId(), city);
        } catch (Exception e) {
            plugin.getLogger().warning("Error loading " + file.getFileName() + ": " + e.getMessage());
        }
    }

    @Override
    public Optional<CityState> get(UUID id) {
        return Optional.ofNullable(cityStates.get(id));
    }

    @Override
    public Optional<CityState> getByName(String name) {
        return cityStates.values().stream()
                .filter(city -> city.getName().equals(name))
                .findFirst();
    }

    @Override
    public Collection<CityState> getAll() {
        return List.copyOf(cityStates.values());
    }

    @Override
    public void save(CityState city) {
        cityStates.put(city.getId(), city);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> saveToFile(city));
    }

    private void saveToFile(CityState city) {
        Path file = cityStatesDir.resolve(city.getName() + ".yml");
        try {
            Files.createDirectories(cityStatesDir);
            try (Writer writer = Files.newBufferedWriter(file)) {
                yaml.dump(city.toMap(), writer);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save " + file.getFileName() + ": " + e.getMessage());
        }
    }

    @Override
    public void saveAll(Collection<CityState> entities) {
        entities.forEach(city -> cityStates.put(city.getId(), city));
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            entities.forEach(this::saveToFile);
        });
    }
}