package com.bentahsin.benthPinata.services;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * config.yml'deki tek bir zamanlanmış görevin tüm yapılandırma verilerini tutar.
 */
public record ScheduledEventConfig(
        String id,
        boolean enabled,
        String pinataType,
        DayOfWeek dayOfWeek, // 'EVERYDAY' ise null olacak
        LocalTime time,
        int minimumPlayers,
        List<Integer> announceBeforeMinutes
) {
}