package com.Momongt.Momongt_MomentRoute.util;

import com.Momongt.Momongt_MomentRoute.entity.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteOptimizer {

    public static double distanceKm(City a, City b) {

        double lat1 = a.getLatitude();
        double lon1 = a.getLongitude();
        double lat2 = b.getLatitude();
        double lon2 = b.getLongitude();

        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double sa = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(sa), Math.sqrt(1 - sa));
        return R * c;
    }

    public static List<City> optimize(List<City> viaCities, City destination) {

        if (viaCities.isEmpty()) return List.of(destination);

        List<List<City>> perms = permutations(viaCities);

        double best = Double.MAX_VALUE;
        List<City> bestRoute = null;

        for (List<City> perm : perms) {
            double total = 0;

            for (int i = 0; i < perm.size() - 1; i++)
                total += distanceKm(perm.get(i), perm.get(i + 1));

            total += distanceKm(perm.get(perm.size() - 1), destination);

            if (total < best) {
                best = total;
                bestRoute = new ArrayList<>(perm);
            }
        }

        bestRoute.add(destination);
        return bestRoute;
    }

    private static List<List<City>> permutations(List<City> list) {
        List<List<City>> result = new ArrayList<>();
        backtrack(list, new boolean[list.size()], new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(List<City> list, boolean[] used,
                                  List<City> current, List<List<City>> result) {
        if (current.size() == list.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (used[i]) continue;
            used[i] = true;
            current.add(list.get(i));
            backtrack(list, used, current, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }
}
