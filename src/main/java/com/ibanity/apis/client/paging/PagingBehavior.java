package com.ibanity.apis.client.paging;

import com.ibanity.apis.client.exceptions.BadRequestException;
import io.crnk.core.engine.query.QueryAdapter;
import io.crnk.core.exception.ParametersDeserializationException;
import io.crnk.core.queryspec.pagingspec.OffsetLimitPagingBehavior;
import io.crnk.core.queryspec.pagingspec.OffsetLimitPagingSpec;
import io.crnk.core.queryspec.pagingspec.PagingSpecUrlBuilder;
import io.crnk.core.resource.links.PagedLinksInformation;
import io.crnk.core.resource.list.ResourceList;
import io.crnk.core.resource.meta.HasMoreResourcesMetaInformation;
import io.crnk.core.resource.meta.PagedMetaInformation;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PagingBehavior extends OffsetLimitPagingBehavior implements io.crnk.core.queryspec.pagingspec.PagingBehavior<OffsetLimitPagingSpec> {

    private final static String LIMIT_PARAMETER     = "limit";
    private static final String BEFORE_PARAMETER    = "before";
    private static final String AFTER_PARAMETER     = "after";

    private static final Integer MAX_PAGE_LIMIT = 100;

    @Override
    public Map<String, Set<String>> serialize(final OffsetLimitPagingSpec offsetLimitPagingSpec, final String resourceType) {

        Map<String, Set<String>> values = new HashMap<>();
        if (offsetLimitPagingSpec.getLimit() != null) {
            values.put(String.format("%s", LIMIT_PARAMETER), new HashSet<>(Arrays.asList(Long.toString(offsetLimitPagingSpec.getLimit()))));
        }
        if (offsetLimitPagingSpec instanceof PagingSpec) {
            PagingSpec pagingSpec = (PagingSpec) offsetLimitPagingSpec;
            if (pagingSpec.getBefore() != null) {
                values.put(String.format("%s", BEFORE_PARAMETER), new HashSet<>(Arrays.asList(pagingSpec.getBefore().toString())));
            }
            if (pagingSpec.getAfter() != null) {
                values.put(String.format("%s", AFTER_PARAMETER), new HashSet<>(Arrays.asList(pagingSpec.getAfter().toString())));
            }
        }

        return values;
    }

    @Override
    public PagingSpec deserialize(final Map<String, Set<String>> parameters) {
        PagingSpec result = createDefaultPagingSpec();

        for (Map.Entry<String, Set<String>> param : parameters.entrySet()) {
            switch(StringUtils.lowerCase(param.getKey())) {
                case LIMIT_PARAMETER :
                    Long limit = getLongValue(param.getKey(), param.getValue());
                    if (MAX_PAGE_LIMIT != null && limit != null && limit > MAX_PAGE_LIMIT) {
                        throw new BadRequestException(
                                String.format("%s legacy value %d is larger than the maximum allowed of %d", LIMIT_PARAMETER, limit, MAX_PAGE_LIMIT)
                        );
                    }
                    result.setLimit(limit);
                    break;
                case BEFORE_PARAMETER :
                    result.setBefore(getUUIDValue(param.getKey(), param.getValue()));
                    break;
                case AFTER_PARAMETER :
                    result.setAfter(getUUIDValue(param.getKey(), param.getValue()));
                    break;
                default:
                    throw new ParametersDeserializationException(param.getKey());
            }
        }

        return result;
    }

    @Override
    public PagingSpec createEmptyPagingSpec() {
        return new PagingSpec();
    }

    @Override
    public PagingSpec createDefaultPagingSpec() {
        return new PagingSpec();
    }

    @Override
    public void build(final PagedLinksInformation linksInformation,
                      final ResourceList<?> resources,
                      final QueryAdapter queryAdapter,
                      final PagingSpecUrlBuilder urlBuilder) {
        Long totalCount = getTotalCount(resources);
        Boolean isNextPageAvailable = isNextPageAvailable(resources);
        if ((totalCount != null || isNextPageAvailable != null) && !hasPageLinks(linksInformation)) {
            // only enrich if not already set
            boolean hasResults = resources.iterator().hasNext();
            doEnrichPageLinksInformation(linksInformation, totalCount, isNextPageAvailable,
                    queryAdapter, hasResults, urlBuilder);
        }
    }

    @Override
    public boolean isRequired(final OffsetLimitPagingSpec offsetLimitPagingSpec) {
        if (offsetLimitPagingSpec instanceof PagingSpec) {
            PagingSpec pagingSpec = (PagingSpec) offsetLimitPagingSpec;
            return pagingSpec.getAfter() !=  null || pagingSpec.getBefore() !=  null || pagingSpec.getLimit() != null;
        }
        else {
            return offsetLimitPagingSpec.getLimit() != null;

        }
    }

    private void doEnrichPageLinksInformation(PagedLinksInformation linksInformation, Long total,
                                              Boolean isNextPageAvailable, QueryAdapter queryAdapter,
                                              boolean hasResults,
                                              PagingSpecUrlBuilder urlBuilder) {
        PagingSpec offsetLimitPagingSpec = (PagingSpec) queryAdapter.getPagingSpec();
        long pageSize = offsetLimitPagingSpec.getLimit();
        UUID after = offsetLimitPagingSpec.getAfter();
        UUID before = offsetLimitPagingSpec.getBefore();
//        long currentPage = offset / pageSize;
//        if (currentPage * pageSize != offset) {
//            throw new BadRequestException("offset " + offset + " is not a multiple of limit " + pageSize);
//        }
//        if (total != null) {
//            isNextPageAvailable = offset + pageSize < total;
//        }
//
//        if (offset > 0 || hasResults) {
//            Long totalPages = total != null ? (total + pageSize - 1) / pageSize : null;
//            QueryAdapter pageSpec = queryAdapter.duplicate();
//            pageSpec.setPagingSpec(new PagingSpec(0L, pageSize));
//            linksInformation.setFirst(urlBuilder.build(pageSpec));
//
//            if (totalPages != null && totalPages > 0) {
//                pageSpec.setPagingSpec(new PagingSpec((totalPages - 1) * pageSize, pageSize));
//                linksInformation.setLast(urlBuilder.build(pageSpec));
//            }
//
//            if (currentPage > 0) {
//                pageSpec.setPagingSpec(new PagingSpec((currentPage - 1) * pageSize, pageSize));
//                linksInformation.setPrev(urlBuilder.build(pageSpec));
//            }
//
//
//            if (isNextPageAvailable) {
//                pageSpec.setPagingSpec(new PagingSpec((currentPage + 1) * pageSize, pageSize));
//                linksInformation.setNext(urlBuilder.build(pageSpec));
//            }
//        }
    }

    private Long getTotalCount(ResourceList<?> resources) {
        PagedMetaInformation pagedMeta = resources.getMeta(PagedMetaInformation.class);
        if (pagedMeta != null) {
            return pagedMeta.getTotalResourceCount();
        }

        return null;
    }

    private Boolean isNextPageAvailable(ResourceList<?> resources) {
        HasMoreResourcesMetaInformation pagedMeta = resources.getMeta(HasMoreResourcesMetaInformation.class);
        if (pagedMeta != null) {
            return pagedMeta.getHasMoreResources();
        }

        return null;
    }

    private boolean hasPageLinks(PagedLinksInformation pagedLinksInformation) {
        return pagedLinksInformation.getFirst() != null || pagedLinksInformation.getLast() != null
                || pagedLinksInformation.getPrev() != null || pagedLinksInformation.getNext() != null;
    }

    private Long getLongValue(final String name, final Set<String> values) {
        if (values.size() > 1) {
            throw new ParametersDeserializationException(name);
        }

        try {
            return Long.parseLong(values.iterator().next());
        } catch (RuntimeException e) {
            throw new ParametersDeserializationException(name);
        }
    }

    private UUID getUUIDValue(final String name, final Set<String> values) {
        if (values.size() > 1) {
            throw new ParametersDeserializationException(name);
        }

        try {
            return UUID.fromString(values.iterator().next());
        } catch (RuntimeException e) {
            throw new ParametersDeserializationException(name);
        }
    }

//    public Integer getDefaultLimit() {
//        return defaultLimit;
//    }
//
//    public void setDefaultLimit(final Integer defaultLimit) {
//        this.defaultLimit = defaultLimit;
//    }
//
//    public Integer getMaxPageLimit() {
//        return maxPageLimit;
//    }
//
//    public void setMaxPageLimit(final Integer maxPageLimit) {
//        this.maxPageLimit = maxPageLimit;
//    }
}