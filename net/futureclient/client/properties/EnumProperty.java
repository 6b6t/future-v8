/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.properties;

import com.gitlab.nuf.exeter.properties.Property;

public class EnumProperty<T extends Enum>
extends Property<T> {
    public EnumProperty(T value, String ... aliases) {
        super(value, aliases);
    }

    public String getFixedValue() {
        return Character.toString(((Enum)this.value).name().charAt(0)) + ((Enum)this.value).name().toLowerCase().replaceFirst(Character.toString(((Enum)this.value).name().charAt(0)).toLowerCase(), "");
    }

    @Override
    public void setValue(String value) {
        Enum[] array = (Enum[])((Enum)this.getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i2 = 0; i2 < length; ++i2) {
            if (!array[i2].name().equalsIgnoreCase(value)) continue;
            this.value = array[i2];
        }
    }

    public void increment() {
        Enum[] array = (Enum[])((Enum)this.getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i2 = 0; i2 < length; ++i2) {
            if (!array[i2].name().equalsIgnoreCase(this.getFixedValue())) continue;
            if (++i2 > array.length - 1) {
                i2 = 0;
            }
            this.setValue(array[i2].toString());
        }
    }

    public void decrement() {
        Enum[] array = (Enum[])((Enum)this.getValue()).getClass().getEnumConstants();
        int length = array.length;
        for (int i2 = 0; i2 < length; ++i2) {
            if (!array[i2].name().equalsIgnoreCase(this.getFixedValue())) continue;
            if (--i2 < 0) {
                i2 = array.length - 1;
            }
            this.setValue(array[i2].toString());
        }
    }
}

