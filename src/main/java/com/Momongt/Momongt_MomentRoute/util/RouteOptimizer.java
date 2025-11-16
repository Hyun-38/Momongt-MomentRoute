package com.Momongt.Momongt_MomentRoute.util;

import com.Momongt.Momongt_MomentRoute.entity.City;

import java.util.ArrayList;
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

        // 경유지가 5개 이상이면 휴리스틱 알고리즘 사용 (성능 개선)
        if (viaCities.size() >= 5) {
            System.out.println("경유지가 " + viaCities.size() + "개이므로 Nearest Neighbor 알고리즘 사용");
            return nearestNeighborOptimize(viaCities, destination);
        } else {
            System.out.println("경유지가 " + viaCities.size() + "개이므로 Brute Force 알고리즘 사용");
            return bruteForceOptimize(viaCities, destination);
        }
    }

    /**
     * Brute Force (완전 탐색) - 모든 순열을 탐색하여 최적 경로 찾기
     * 시간복잡도: O(n!)
     * 경유지가 적을 때(4개 이하) 사용
     */
    private static List<City> bruteForceOptimize(List<City> viaCities, City destination) {
        List<List<City>> perms = permutations(viaCities);

        double best = Double.MAX_VALUE;
        List<City> bestRoute = new ArrayList<>();

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

    /**
     * Nearest Neighbor (최근접 이웃) - 휴리스틱 알고리즘
     * 시간복잡도: O(n²)
     * 경유지가 많을 때(5개 이상) 사용
     * 항상 최적은 아니지만 합리적인 경로를 빠르게 찾음
     */
    private static List<City> nearestNeighborOptimize(List<City> viaCities, City destination) {
        List<City> remaining = new ArrayList<>(viaCities);
        List<City> route = new ArrayList<>();

        // 첫 번째 도시를 임의로 선택 (첫 번째 경유지)
        City current = remaining.remove(0);
        route.add(current);

        // 남은 도시들 중 가장 가까운 도시를 계속 선택
        while (!remaining.isEmpty()) {
            City nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (City city : remaining) {
                double dist = distanceKm(current, city);
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = city;
                }
            }

            route.add(nearest);
            remaining.remove(nearest);
            current = nearest;
        }

        // 마지막에 목적지 추가
        route.add(destination);
        return route;
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
