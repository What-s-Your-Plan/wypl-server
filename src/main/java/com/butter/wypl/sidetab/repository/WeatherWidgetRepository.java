package com.butter.wypl.sidetab.repository;

import org.springframework.data.repository.CrudRepository;

import com.butter.wypl.infrastructure.weather.WeatherRegion;
import com.butter.wypl.sidetab.domain.cache.WeatherWidget;

public interface WeatherWidgetRepository extends CrudRepository<WeatherWidget, WeatherRegion> {
}
