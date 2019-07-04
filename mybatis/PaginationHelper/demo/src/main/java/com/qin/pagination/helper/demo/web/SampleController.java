package com.qin.pagination.helper.demo.web;

import com.qin.pagination.helper.demo.model.Page;
import com.qin.pagination.helper.demo.model.TaskCronJob;
import com.qin.pagination.helper.demo.utils.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class SampleController {

    @Autowired
    private JdbcTemplate jt;

    private static final RowMapper<TaskCronJob> rowMapper = new RowMapper<TaskCronJob>() {
        @Override
        public TaskCronJob mapRow(ResultSet rs, int rowNum) throws SQLException {
            TaskCronJob job = new TaskCronJob();
            job.setId(rs.getLong("id"));
            job.setCron(rs.getString("cron"));
            return job;
        }
    };

    @RequestMapping(value = "/sample", method = RequestMethod.GET)
    public Page<TaskCronJob> page() {

        PaginationHelper helper = new PaginationHelper();

        final String sqlCountRows = "select count(*) from task_cron_job";
        final String sqlFetchRows = "select * from task_cron_job";

        final Object[] args = null;
        final int pageNo = 1;
        final int pageSize = 3;
        RowMapper<TaskCronJob> rm = new BeanPropertyRowMapper<>(TaskCronJob.class);

        Page<TaskCronJob> result = helper.fetchPage(jt, sqlCountRows, sqlFetchRows, args, pageNo, pageSize, rm);

        return result;
    }
}
