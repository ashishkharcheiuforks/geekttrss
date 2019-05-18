/*
 * Geekttrss is a RSS feed reader application on the Android Platform.
 *
 * Copyright (C) 2017-2019 by Frederic-Charles Barthelery.
 *
 * This file is part of Geekttrss.
 *
 * Geekttrss is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Geekttrss is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Geekttrss.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.geekorum.ttrss.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Factory that can creates the {@link ViewModel} needed by the application after injecting them.
 */
public  class ViewModelsFactory implements ViewModelProvider.Factory {

    private Map<Class<?>, Provider<ViewModel>> providers;

    @Inject
    public ViewModelsFactory(Map<Class<?>, Provider<ViewModel>> providers) {
        this.providers = providers;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<ViewModel> viewModelProvider = providers.get(modelClass);
        if (viewModelProvider != null) {
            return (T) viewModelProvider.get();
        }
        throw new IllegalArgumentException("unknown model class " + modelClass);
    }
}