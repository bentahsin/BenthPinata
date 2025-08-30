package com.bentahsin.benthPinata.services;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * config.yml'deki tek bir zamanlanmış görevin tüm yapılandırma verilerini tutar.
 */
public final class ScheduledEventConfig {

    private final String id;
    private final boolean enabled;
    private final String pinataType;
    private final DayOfWeek dayOfWeek; // 'EVERYDAY' ise null olacak
    private final LocalTime time;
    private final int minimumPlayers;
    private final List<Integer> announceBeforeMinutes;

    public ScheduledEventConfig(String id, boolean enabled, String pinataType, DayOfWeek dayOfWeek,
                                LocalTime time, int minimumPlayers, List<Integer> announceBeforeMinutes) {
        this.id = id;
        this.enabled = enabled;
        this.pinataType = pinataType;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.minimumPlayers = minimumPlayers;
        this.announceBeforeMinutes = announceBeforeMinutes;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPinataType() {
        return pinataType;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public List<Integer> getAnnounceBeforeMinutes() {
        return announceBeforeMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledEventConfig that = (ScheduledEventConfig) o;
        return enabled == that.enabled &&
                minimumPlayers == that.minimumPlayers &&
                Objects.equals(id, that.id) &&
                Objects.equals(pinataType, that.pinataType) &&
                dayOfWeek == that.dayOfWeek &&
                Objects.equals(time, that.time) &&
                Objects.equals(announceBeforeMinutes, that.announceBeforeMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enabled, pinataType, dayOfWeek, time, minimumPlayers, announceBeforeMinutes);
    }

    @Override
    public String toString() {
        return "ScheduledEventConfig{" +
                "id='" + id + '\'' +
                ", enabled=" + enabled +
                ", pinataType='" + pinataType + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", time=" + time +
                ", minimumPlayers=" + minimumPlayers +
                ", announceBeforeMinutes=" + announceBeforeMinutes +
                '}';
    }
}