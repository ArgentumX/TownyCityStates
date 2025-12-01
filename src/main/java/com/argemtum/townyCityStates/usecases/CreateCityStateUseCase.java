package com.argemtum.townyCityStates.usecases;

import com.argemtum.townyCityStates.adapters.WorldGuardAdapter;
import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.config.language.MessageNode;
import com.argemtum.townyCityStates.exceptions.absctraction.CityStatesException;
import com.argemtum.townyCityStates.objects.CityState;
import com.argemtum.townyCityStates.repositories.abstraction.ICityStateRepository;
import com.argemtum.townyCityStates.repositories.abstraction.ILocalizationRepository;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CreateCityStateUseCase {
    private final ICityStateRepository cityStateRepository;
    private final Localization localization;
    private final WorldGuardAdapter wgAdapter;

    @Inject
    public CreateCityStateUseCase(
            ICityStateRepository cityStateRepository,
            ILocalizationRepository localizationRepository,
            WorldGuardAdapter wgAdapter
    ) {
        this.cityStateRepository = cityStateRepository;
        this.localization = localizationRepository.GetInstance();
        this.wgAdapter = wgAdapter;
    }

    public void createCityState(String name) throws CityStatesException {
        validateCityName(name);

        CityState cityState = new CityState(name);
        cityStateRepository.save(cityState);
    }

    public CityState createCityStateWithExistingRegion(Player player, String name, String regionName) throws CityStatesException {
        validateCityName(name);

        World world = player.getWorld();
        if (!wgAdapter.hasRegion(world, regionName.toLowerCase())) {
            throw new CityStatesException(localization.of(MessageNode.REGION_NOT_FOUND));
        }

        CityState cityState = new CityState(name, regionName);
        cityStateRepository.save(cityState);
        return cityState;
    }

    public CityState createCityStateWithAutoRegion(Player player, String name, int radius) throws CityStatesException {
        validateCityName(name);

        World world = player.getWorld();
        String regionNameId = name.toLowerCase();
        if (wgAdapter.hasRegion(world, regionNameId)) {
            throw new CityStatesException(localization.of(MessageNode.REGION_ALREADY_EXISTS, regionNameId));
        }

        Location loc = player.getLocation();
        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();

        wgAdapter.createChunkCubeRegion(world, regionNameId, cx, cz, radius);

        CityState cityState = new CityState(name, regionNameId);
        cityStateRepository.save(cityState);
        return cityState;
    }

    private void validateCityName(String name) throws CityStatesException {
        if (cityStateRepository.getByName(name).isPresent()) {
            throw new CityStatesException(localization.of(MessageNode.CITY_ALREADY_EXISTS));
        }
    }
}