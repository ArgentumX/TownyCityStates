package com.argemtum.townyCityStates.repositories.abstraction;

import com.argemtum.townyCityStates.config.language.Localization;
import com.argemtum.townyCityStates.repositories.abstraction.abstraction.IIdentifiableRepository;

public interface ILocalizationRepository {
    Localization GetInstance();
    void load(String id);
}
