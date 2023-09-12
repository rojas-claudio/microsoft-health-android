package com.microsoft.kapp.services.weather;

import com.microsoft.kapp.services.ServiceException;
import java.util.List;
/* loaded from: classes.dex */
public interface WeatherService {
    List<WeatherDay> getDailyWeather(double d, double d2, String str) throws ServiceException;
}
