package com.mn.broker.store;

import com.mn.broker.model.WatchList;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class InMemoryAccountStore {

    private Map<UUID, WatchList> watchListMap = new HashMap<>();

    public WatchList getWatchList(UUID accountID) {
        return watchListMap.getOrDefault(accountID, new WatchList());
    }

    public WatchList updateWatchList(UUID randomUUID, WatchList watchList) {
        watchListMap.put(randomUUID, watchList);
        return watchListMap.get(randomUUID);
    }

    public void deleteWatchList(UUID accountId) {
        watchListMap.remove(accountId);
    }
}
