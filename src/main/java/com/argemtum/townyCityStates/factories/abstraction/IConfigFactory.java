package com.argemtum.townyCityStates.factories.abstraction;

import com.argemtum.townyCityStates.config.settings.TownyCityStatesSettings;

public interface IConfigFactory {
    TownyCityStatesSettings generateDefaultSetting();
}
