/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thomaskint.minidao.connection.MDConnection;
import com.thomaskint.minidao.connection.MDConnectionConfig;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.exception.MDNotAnMDEntityException;
import com.thomaskint.minidao.exception.MDParamNotIncludedInClassException;
import com.thomaskint.minidao.model.MDCallStack;
import com.thomaskint.minidao.model.MDEntityInfo;
import com.thomaskint.minidao.querybuilder.MDCondition;
import com.thomaskint.minidao.querybuilder.MDSelectBuilder;
import com.thomaskint.minidao.utils.MDStringUtils;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;
import static com.thomaskint.minidao.enumeration.MDSQLAction.SELECT;

/**
 * Class exposing methods to read entities from database
 *
 * @author Thomas Kint
 */
public class MDRead extends MDCRUDBase {

	public MDRead(MDConnectionConfig connectionConfig) {
		super(connectionConfig);
	}

	/**
	 * Retrieve entity based on its class and primary key
	 *
	 * @param entityClass {@link Class}
	 * @param id          {@link Object}
	 * @param <T>         T
	 * @return Entity
	 * @throws MDException when can't retrieve data
	 */
	public <T> T getEntityById(Class<T> entityClass, Object id) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);

		String fieldName = entityInfo.getIDFieldInfo().getFieldName();

		MDCondition condition = new MDCondition(fieldName, EQUAL, id);

		return getEntityByCondition(entityClass, condition);
	}

	/**
	 * Retrieve entity based on its class and given condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param <T>         T
	 * @return Entity
	 * @throws MDException when can't retrieve data
	 */
	public <T> T getEntityByCondition(Class<T> entityClass, MDCondition condition) throws MDException {
		List<T> entities = getEntities(entityClass, condition, null);

		T entity = null;

		if (entities.size() > 0) {
			entity = entities.get(0);
		}

		return entity;
	}

	/**
	 * Retrieve entity based on its class and given condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param <T>         T
	 * @return Entity
	 * @throws MDException when can't retrieve data
	 */
	public <T> T getEntityByCondition(Class<T> entityClass, MDCondition condition, MDCallStack<Class> callStack)
			throws MDException {
		List<T> entities = getEntities(entityClass, condition, callStack);

		T entity = null;

		if (entities.size() > 0) {
			entity = entities.get(0);
		}

		return entity;
	}

	/**
	 * Retrieve entities based on class
	 *
	 * @param entityClass {@link Class}
	 * @param <T>         T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	public <T> List<T> getEntities(Class<T> entityClass) throws MDException {
		return getEntities(entityClass, null);
	}

	/**
	 * Retrieve entities based on class and condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param <T>         T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	public <T> List<T> getEntities(Class<T> entityClass, MDCondition condition) throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return getEntities(entityInfo, condition);
	}

	/**
	 * Retrieve entities based on class and condition
	 *
	 * @param entityClass {@link Class}
	 * @param condition   {@link MDCondition}
	 * @param callStack   {@link Class}
	 * @param <T>         T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	public <T> List<T> getEntities(Class<T> entityClass, MDCondition condition, MDCallStack<Class> callStack)
			throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return getEntities(entityInfo, condition, callStack);
	}

	/**
	 * Retrieve entities based on entityInfo
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param <T>        T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	private <T> List<T> getEntities(MDEntityInfo entityInfo) throws MDException {
		return getEntities(entityInfo, null);
	}

	/**
	 * Retrieve entities based on entityInfo and condition
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param condition  {@link MDCondition}
	 * @param <T>        T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	private <T> List<T> getEntities(MDEntityInfo entityInfo, MDCondition condition) throws MDException {
		return getEntities(entityInfo, condition, null);
	}

	/**
	 * Retrieve entities based on entityInfo and condition
	 *
	 * @param entityInfo {@link MDEntityInfo}
	 * @param condition  {@link MDCondition}
	 * @param callStack  {@link MDCallStack}
	 * @param <T>        T
	 * @return List of entities
	 * @throws MDException when can't retrieve data
	 */
	private <T> List<T> getEntities(MDEntityInfo entityInfo, MDCondition condition, MDCallStack<Class> callStack)
			throws MDException {
		// Control if it's an MDEntity
		if (!entityInfo.isMDEntity()) {
			throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
		}
		if (!entityInfo.isSQLActionAllowed(SELECT)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), SELECT);
		}

		if (callStack == null) {
			callStack = new MDCallStack<>();
		}
		callStack.push(entityInfo.getEntityClass());

		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(entityInfo.getEntityClass());

		// Condition
		if (condition != null) {
			selectBuilder.where(condition);
		}

		// InheritLink
		MDEntityInfo parentEntityInfo = entityInfo.getParentEntityInfo();
		if (parentEntityInfo != null) {
			selectBuilder.innerJoin(parentEntityInfo.getEntityClass());
		}

		// Build query
		String query = selectBuilder.build();

		// Execute query
		ResultSet resultSet;
		resultSet = MDConnection.executeQuery(connectionConfig, query);

		List<T> entities = new ArrayList<>();
		try {
			// Map resultset to the expected objet
			while (resultSet.next()) {
				entities.add(entityInfo.mapEntity(resultSet, this, callStack));
			}
			callStack.pop();
			resultSet.close();
		} catch (SQLException e) {
			throw new MDException(e);
		}
		return entities;
	}

	public <T> int countEntities(Class<T> entityClass)
			throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return countEntities(entityInfo);
	}

	public <T> int countEntities(Class<T> entityClass, MDCondition condition)
			throws MDException {
		MDEntityInfo entityInfo = new MDEntityInfo(entityClass);
		return countEntities(entityInfo, condition);
	}

	private int countEntities(MDEntityInfo entityInfo) throws MDException {
		return countEntities(entityInfo, null);
	}

	private int countEntities(MDEntityInfo entityInfo, MDCondition condition) throws MDException {
		// Control if it's an MDEntity
		if (!entityInfo.isMDEntity()) {
			throw new MDNotAnMDEntityException(entityInfo.getEntityClass());
		}
		if (!entityInfo.isSQLActionAllowed(SELECT)) {
			throw new MDParamNotIncludedInClassException(entityInfo.getEntityClass(), SELECT);
		}

		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.count(entityInfo.getEntityClass());

		// Condition
		if (condition != null) {
			selectBuilder.where(condition);
		}

		// Build query
		String query = selectBuilder.build();

		// Execute query
		ResultSet resultSet;
		resultSet = MDConnection.executeQuery(connectionConfig, query);

		int count = 0;
		try {
			if (resultSet.next()) {
				count = resultSet.getInt(MDStringUtils.TOTAL);
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new MDException(e);
		}
		return count;
	}
}
