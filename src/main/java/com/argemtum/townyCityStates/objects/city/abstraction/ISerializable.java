package com.argemtum.townyCityStates.objects.city.abstraction;

import java.util.Map;

public interface ISerializable<T> {
    Map<String, Object> serialize();
}
