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

package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.enumeration.MDConditionLink;
import com.thomaskint.minidao.exception.MDException;
import com.thomaskint.minidao.testonly.model.ContactTest;
import com.thomaskint.minidao.testonly.model.MessageTest;
import com.thomaskint.minidao.testonly.model.PlayerTest;
import com.thomaskint.minidao.testonly.model.UserTest;
import org.junit.Assert;
import org.junit.Test;

import static com.thomaskint.minidao.enumeration.MDConditionOperator.EQUAL;

public class MDSelectBuilderTest {

	@Test
	public void should_construct_query() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_without_verb() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_one_specific_field() {
		// GIVEN
		String expectedQuery = "SELECT message.id_message FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select("id_message").from(MessageTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_specific_fields_unordered_inner_join() {
		// GIVEN
		String expectedQuery = "SELECT message.id_message FROM message INNER JOIN user ON message.id_author = user.id_user";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class).select("id_message").innerJoin(UserTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_many_specific_fields() {
		// GIVEN
		String expectedQuery = "SELECT message.id_message, message.id_author, message.content FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class).select("id_message", "id_author", "content");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_many_specific_fields_valid() {
		// GIVEN
		String expectedQuery = "SELECT message.id_message FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class).select("id_message", "invalid_field");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_specific_field_invalid() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class).select("invalid_field");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_innerjoin_and_many_specific_fields() {
		// GIVEN
		String expectedQuery = "SELECT message.id_message, message.id_author, user.pseudo FROM message INNER JOIN user ON message.id_author = user.id_user";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class).innerJoin(UserTest.class).select("id_message", "id_author", "pseudo");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_innerjoin_and_many_specific_fields_invalid() {
		// GIVEN
		String expectedQuery = "SELECT message.id_message, message.id_author FROM message INNER JOIN user ON message.id_author = user.id_user";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.from(MessageTest.class).innerJoin(UserTest.class).select("id_message", "id_author", "invalid_field");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_condition() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message WHERE message.id_message = 1";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).where("id_message", EQUAL, 1);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_nested_conditions() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message WHERE (message.id_message = 1 OR message.id_author = 1) AND message.content = 'BOB'";

		MDCondition subCondition = new MDCondition("id_author", EQUAL, 1);
		MDCondition condition = new MDCondition("id_message", EQUAL, 1, MDConditionLink.OR, subCondition);

		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).where(condition).and("content", EQUAL, "BOB");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_multiple_conditions() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message WHERE message.id_message = 1 AND message.id_author = 1";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).where("id_message", EQUAL, 1).and("id_author", EQUAL, 1);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_inner_join_and_condition() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message INNER JOIN user ON message.id_author = user.id_user WHERE message.id_message = 1";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).innerJoin(UserTest.class).where("id_message", EQUAL, 1);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_without_condition_when_not_valid() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message INNER JOIN user ON message.id_author = user.id_user";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).innerJoin(UserTest.class).where("id_player", EQUAL, 1);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_with_inner_join_and_condition_on_join() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message INNER JOIN user ON message.id_author = user.id_user WHERE user.pseudo = 'pseudo01'";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).innerJoin(UserTest.class).where("pseudo", EQUAL, "pseudo01");
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_and_ignore_join_if_not_valid() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).innerJoin(PlayerTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_many_to_one() {
		// GIVEN
		String expectedQuery = "SELECT * FROM message INNER JOIN user ON message.id_author = user.id_user";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(MessageTest.class).innerJoin(UserTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}

	@Test
	public void should_construct_query_inherit() {
		// GIVEN
		String expectedQuery = "SELECT * FROM player INNER JOIN contact ON player.id_contact_parent = contact.id_contact";
		MDSelectBuilder selectBuilder = new MDSelectBuilder();
		selectBuilder.select().from(PlayerTest.class).innerJoin(ContactTest.class);
		// WHEN
		String returnedQuery = null;
		try {
			returnedQuery = selectBuilder.build();
		} catch (MDException e) {
			e.printStackTrace();
		}
		// THEN
		Assert.assertEquals(expectedQuery, returnedQuery);
	}
}