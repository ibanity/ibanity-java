package com.ibanity.apis.client.paging;

import com.ibanity.apis.client.exceptions.ClientBadRequestException;
import io.crnk.core.exception.ParametersDeserializationException;
import io.crnk.core.queryspec.pagingspec.OffsetLimitPagingBehavior;
import io.crnk.core.queryspec.pagingspec.OffsetLimitPagingSpec;
import io.crnk.core.queryspec.pagingspec.PagingBehavior;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IbanityPagingBehavior extends OffsetLimitPagingBehavior implements PagingBehavior<OffsetLimitPagingSpec> {

    private static final String LIMIT_PARAMETER     = "limit";
    private static final String BEFORE_PARAMETER    = "before";
    private static final String AFTER_PARAMETER     = "after";

    private static final Integer MAX_PAGE_LIMIT = 100;

    @Override
    public Map<String, Set<String>> serialize(final OffsetLimitPagingSpec offsetLimitPagingSpec, final String resourceType) {

        Map<String, Set<String>> values = new HashMap<>();
        if (offsetLimitPagingSpec.getLimit() != null) {
            values.put(String.format("%s", LIMIT_PARAMETER), new HashSet<>(Arrays.asList(Long.toString(offsetLimitPagingSpec.getLimit()))));
        }
        if (offsetLimitPagingSpec instanceof IbanityPagingSpec) {
            IbanityPagingSpec pagingSpec = (IbanityPagingSpec) offsetLimitPagingSpec;
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
    public IbanityPagingSpec deserialize(final Map<String, Set<String>> parameters) {
        IbanityPagingSpec result = createDefaultPagingSpec();

        for (Map.Entry<String, Set<String>> param : parameters.entrySet()) {
            switch (StringUtils.lowerCase(param.getKey())) {
                case LIMIT_PARAMETER :
                    Long limit = getLongValue(param.getKey(), param.getValue());
                    if (limit != null && limit > MAX_PAGE_LIMIT) {
                        throw new ClientBadRequestException(
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
    public IbanityPagingSpec createEmptyPagingSpec() {
        return new IbanityPagingSpec();
    }

    @Override
    public IbanityPagingSpec createDefaultPagingSpec() {
        return new IbanityPagingSpec();
    }

    @Override
    public boolean isRequired(final OffsetLimitPagingSpec offsetLimitPagingSpec) {
        if (offsetLimitPagingSpec instanceof IbanityPagingSpec) {
            IbanityPagingSpec pagingSpec = (IbanityPagingSpec) offsetLimitPagingSpec;
            return pagingSpec.getAfter() !=  null || pagingSpec.getBefore() !=  null || pagingSpec.getLimit() != null;
        } else {
            return offsetLimitPagingSpec.getLimit() != null;

        }
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
}
