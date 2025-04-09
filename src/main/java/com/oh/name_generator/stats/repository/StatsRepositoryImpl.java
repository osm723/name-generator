package com.oh.name_generator.stats.repository;


import com.oh.name_generator.stats.dto.StatsRequestCond;
import com.oh.name_generator.stats.dto.StatsResponseDto;
import com.oh.name_generator.stats.entity.NameStats;
import com.oh.name_generator.stats.entity.QNameStats;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oh.name_generator.stats.entity.QNameStats.nameStats;


@Repository
@Slf4j
public class StatsRepositoryImpl implements StatsRepositoryQuery {

    private JPAQueryFactory queryFactory;
    public StatsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<StatsResponseDto> findByCond(Pageable pageable, StatsRequestCond statsRequestCond) {
        QNameStats nameStats_1 = nameStats;
        QNameStats nameStats_2 = new QNameStats("nameStatsSub");

        QueryResults<StatsResponseDto> results = queryFactory
                .select(Projections.constructor(StatsResponseDto.class,
                        nameStats_1.name,
                        nameStats_1.gender,
                        nameStats_1.years,
                        nameStats_1.yearRank,
                        nameStats_1.yearCount,
                        JPAExpressions
                                .select(nameStats_2.yearCount.sum())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.avg().castToNum(Double.class).round())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.max())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.min())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.count())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name))
                ))
                .from(nameStats_1)
                .where(genderEqual(statsRequestCond.getGender())
                        , nameEqual(statsRequestCond.getName())
                        , startYearGoe(statsRequestCond.getStartYear())
                        , endYearGoe(statsRequestCond.getEndYear())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable))
                .fetchResults();

            // JPAExpressions alias가 안됨
//        QueryResults<StatsResponseDto> results = queryFactory
//                .select(Projections.constructor(StatsResponseDto.class,
//                        nameStats_1.name,
//                        nameStats_1.gender,
//                        nameStats_1.years,
//                        nameStats_1.yearRank,
//                        nameStats_1.yearCount,
//                        nameStats_2.yearCount.sum().as("totalCount"),
//                        nameStats_2.yearRank.avg().castToNum(Double.class).round().as("totalAvgRank"),
//                        nameStats_2.yearRank.min().as("totalMinRank"),
//                        nameStats_2.yearRank.max().as("totalMaxRank")
//                ))
//                .from(nameStats_1)
//                .innerJoin(
//                        JPAExpressions
//                                .select(
//                                        Projections.constructor(StatsResponseDto.class,
//                                                nameStats_2.name,
//                                                nameStats_2.yearCount.sum().as("totalCount"),
//                                                nameStats_2.yearRank.avg().castToNum(Double.class).round().as("totalAvgRank"),
//                                                nameStats_2.yearRank.min().as("totalMinRank"),
//                                                nameStats_2.yearRank.max().as("totalMaxRank")
//                                        )
//                                )
//                                .from(nameStats_2)
//                                .groupBy(nameStats_2.name)
//                                .as("b")
//                )
//                .where(nameStats_1.name.eq(nameStats_2.name))
//                .offset(pageable.getOffset())  // 페이징 시작 위치
//                .limit(pageable.getPageSize())  // 페이징 크기
//                .fetchResults();

        List<StatsResponseDto> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<NameStats> findByWhere(Pageable pageable, StatsRequestCond statsRequestCond) {
        QNameStats nameStats_1 = nameStats;
        QNameStats nameStats_2 = new QNameStats("nameStatsSub");

        QueryResults<NameStats> results = queryFactory
                .select(Projections.constructor(NameStats.class,
                        nameStats_1.name,
                        nameStats_1.gender,
                        nameStats_1.years,
                        nameStats_1.yearRank,
                        nameStats_1.yearCount,
                        JPAExpressions
                                .select(nameStats_2.yearCount.sum())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.avg().castToNum(Double.class).round())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.max())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.min())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name)),
                        JPAExpressions
                                .select(nameStats_2.yearRank.count())
                                .from(nameStats_2)
                                .where(nameStats_2.name.eq(nameStats_1.name))
                ))
                .from(nameStats_1)
                .where(genderEqual(statsRequestCond.getGender())
                        , nameEqual(statsRequestCond.getName())
                        , startYearGoe(statsRequestCond.getStartYear())
                        , endYearGoe(statsRequestCond.getEndYear())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable))
                .fetchResults();

        List<NameStats> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Map<Integer, Long> findYearCountByName(StatsRequestCond statsRequestCond) {
        List<Tuple> results = queryFactory
                .select(nameStats.years, nameStats.yearCount)
                .from(nameStats)
                .where(nameEqual(statsRequestCond.getName()))
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(nameStats.years),
                        tuple -> tuple.get(nameStats.yearCount)
                ));
    }

    @Override
    public Map<Integer, Integer> findYearRankByName(StatsRequestCond statsRequestCond) {
        List<Tuple> results = queryFactory
                .select(nameStats.years, nameStats.yearRank)
                .from(nameStats)
                .where(nameEqual(statsRequestCond.getName()))
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(nameStats.years),
                        tuple -> tuple.get(nameStats.yearRank)
                ));
    }

    /**
     * 정렬처리 함수 (기본 필드 + 그룹함수 필드 처리)
     * getOrderSpecifier
     * @param pageable
     * @return
     */
    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        QNameStats nameStats_1 = nameStats;
        QNameStats nameStats_2 = new QNameStats("nameStatsSub");

        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            switch (property) {
                case "totalCount":
                    orders.add(new OrderSpecifier<>(
                            isAscending ? Order.ASC : Order.DESC,
                            JPAExpressions
                                    .select(nameStats_2.yearCount.sum())
                                    .from(nameStats_2)
                                    .where(nameStats_2.name.eq(nameStats_1.name))
                    ));
                    break;

                case "totalAvgRank":
                    orders.add(new OrderSpecifier<>(
                            isAscending ? Order.ASC : Order.DESC,
                            JPAExpressions
                                    .select(nameStats_2.yearRank.avg().castToNum(Double.class))
                                    .from(nameStats_2)
                                    .where(nameStats_2.name.eq(nameStats_1.name))
                    ));
                    break;

                case "totalMaxRank":
                    orders.add(new OrderSpecifier<>(
                            isAscending ? Order.ASC : Order.DESC,
                            JPAExpressions
                                    .select(nameStats_2.yearRank.max())
                                    .from(nameStats_2)
                                    .where(nameStats_2.name.eq(nameStats_1.name))
                    ));
                    break;

                case "totalMinRank":
                    orders.add(new OrderSpecifier<>(
                            isAscending ? Order.ASC : Order.DESC,
                            JPAExpressions
                                    .select(nameStats_2.yearRank.min())
                                    .from(nameStats_2)
                                    .where(nameStats_2.name.eq(nameStats_1.name))
                    ));
                    break;

                case "totalRankCount":
                    orders.add(new OrderSpecifier<>(
                            isAscending ? Order.ASC : Order.DESC,
                            JPAExpressions
                                    .select(nameStats_2.yearRank.count())
                                    .from(nameStats_2)
                                    .where(nameStats_2.name.eq(nameStats_1.name))
                    ));
                    break;

                default:
                    // 기본 필드 정렬 처리
                    orders.add(getDefaultOrderSpecifier(order));
                    break;
            }
        }

        return orders.toArray(new OrderSpecifier<?>[0]);
    }

    /**
     * 기본 정렬처리 함수
     * getDefaultOrderSpecifier
     * @param order
     * @return
     */
    private OrderSpecifier<?> getDefaultOrderSpecifier(Sort.Order order) {
        String sortField = order.getProperty();
        boolean isAscending = order.getDirection().isAscending();

        if ("years".equalsIgnoreCase(sortField)) {
            return isAscending ? nameStats.years.asc() : nameStats.years.desc();
        } else if ("gender".equalsIgnoreCase(sortField)) {
            return isAscending ? nameStats.gender.asc() : nameStats.gender.desc();
        } else if ("yearCount".equalsIgnoreCase(sortField)) {
            return isAscending ? nameStats.yearCount.asc() : nameStats.yearCount.desc();
        } else if ("yearRank".equalsIgnoreCase(sortField)) {
            return isAscending ? nameStats.yearRank.asc() : nameStats.yearRank.desc();
        }

        // 기본 정렬 기준
        return nameStats.years.desc();
    }

    public BooleanExpression genderEqual(String gender) {
        return StringUtils.hasText(gender) ? nameStats.gender.eq(gender) : null;
    }

    public BooleanExpression nameEqual(String name) {
        return StringUtils.hasText(name) ? nameStats.name.eq(name) : null;
    }
    private BooleanExpression startYearGoe(Integer startYear) {
        return startYear != null ? nameStats.years.goe(startYear) : null;
    }

    private BooleanExpression endYearGoe(Integer endYear) {
        return endYear != null ? nameStats.years.loe(endYear) : null;
    }


//    @Override
//    public Page<NameStats> findByWhere(Pageable pageable, StatsRequestCond statsRequestCond) {
//        QueryResults<NameStats> results = queryFactory
//                .select(nameStats)
//                .from(nameStats)
//                .where(genderEqual(statsRequestCond.getGender())
//                        , nameEqual(statsRequestCond.getName())
//                        , startYearGoe(statsRequestCond.getStartYear())
//                        , endYearGoe(statsRequestCond.getEndYear())
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(getOrderSpecifier(pageable))
//                .fetchResults();
//
//        List<NameStats> contents = results.getResults();
//        long total = results.getTotal();
//
//        return new PageImpl<>(contents, pageable, total);
//    }

}
