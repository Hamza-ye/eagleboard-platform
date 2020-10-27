package com.mass3d.query.planner;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import com.mass3d.query.Query;
import com.mass3d.schema.Schema;

public interface QueryPlanner
{
    QueryPlan planQuery(Query query);

    QueryPlan planQuery(Query query, boolean persistedOnly);

    QueryPath getQueryPath(Schema schema, String path);

    Path<?> getQueryPath(Root<?> root, Schema schema, String path);
}
