package com.thomaskint.minidao.query.delete;

import com.thomaskint.minidao.MDConnection;
import com.thomaskint.minidao.query.MDCondition;
import com.thomaskint.minidao.utils.MDEntityUtils;
import com.thomaskint.minidao.utils.MDFieldUtils;

import static com.thomaskint.minidao.enumeration.MDParam.DELETE;

/**
 * Created by tkint on 19/01/2018.
 */
public class MDDelete {

	public static <T> boolean deleteEntities(Class<T> entityClass, MDCondition mdCondition) throws Exception {
		if (!MDEntityUtils.includeParam(entityClass, DELETE)) {
			throw new Exception("");
		}

		boolean deleted;

		StringBuilder queryBuilder = new StringBuilder("DELETE FROM ");

		// Adding table
		queryBuilder.append(MDEntityUtils.getTableName(entityClass));

		// Adding conditions
		if (mdCondition != null) {
			queryBuilder.append(" WHERE ");
			queryBuilder.append(mdCondition.build());
		}

		deleted = MDConnection.getInstance().executeUpdate(queryBuilder.toString()) > 0;

		return deleted;
	}

	public static <T> boolean deleteEntity(Class<T> entityClass, int id) throws Exception {
		MDCondition mdCondition = new MDCondition(MDFieldUtils.getIdFieldName(entityClass), id);
		return deleteEntities(entityClass, mdCondition);
	}
}
