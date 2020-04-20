package com.backtolife.survey.util;

import androidx.annotation.Nullable;

public class Id {
    public int id;

    public Id(int id){
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj) || (obj instanceof Id && id == ((Id) obj).id);
    }
}
