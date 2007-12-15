package org.springframework.batch.sample;

import javax.sql.DataSource;

import org.springframework.batch.sample.item.processor.StagingItemProcessor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class FootballJobFunctionalTests extends
		AbstractValidatingBatchLauncherTests {

	private JdbcOperations jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	protected String[] getConfigLocations() {
		return new String[] { "jobs/parallelJob.xml##" };
	}

	protected void validatePostConditions() throws Exception {
		int count;
		count = jdbcTemplate.queryForInt(
				"SELECT COUNT(*) from BATCH_STAGING where PROCESSED=?",
				new Object[] {StagingItemProcessor.NEW});
		assertEquals(0, count);
		int total = jdbcTemplate.queryForInt(
				"SELECT COUNT(*) from BATCH_STAGING");
		count = jdbcTemplate.queryForInt(
				"SELECT COUNT(*) from BATCH_STAGING where PROCESSED=?",
				new Object[] {StagingItemProcessor.DONE});
		assertEquals(total, count);
	}

}
