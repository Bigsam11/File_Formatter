package com.ubagroup.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ubagroup.dao.FileDao;
import com.ubagroup.model.File;

@Repository
public class FileDaoImpl extends JdbcDaoSupport implements FileDao{


    @Autowired
    DataSource dataSource;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HHmmss");
    Date curDate = new Date();
    String strDate = sdf.format(curDate);

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public void insertFile(File emp) {
        String sql = "INSERT INTO MT950_FILEFORMATTER " +
                "(ID,FILE_NAME,FILE_CONTENT,INPUTTER_NAME,INPUTTED_DATE) VALUES (crm_seq.nextval, ?,?,?,sysdate)" ;
        getJdbcTemplate().update(sql, new Object[]{
                 emp.getFile_name(),emp.getFile_content(),emp.getInputter_name()
        });
    }

    @Override
    public void insertFiles(List<File> files) {

    }

    @Override
    public void insertFile(final List<File> files) {
        String sql = "INSERT INTO Formatted " + "(ID,FILE_NAME,FILE_CONTENT,INPUTTER_NAME,INPUTTED_DATE) VALUES (crm_seq.nextval, ?,?,?,sysdate)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                File file = files.get(i);
              //  ps.setString(1, String.valueOf(file.getId()));
                ps.setString(1, file.getFile_name());
                ps.setString(2, String.valueOf(file.getFile_content()));
                ps.setString(3, "Tobiloba");
             //   ps.setString(4, sdf.format(file.getInputted_date()));
            }

            public int getBatchSize() {
                return files.size();
            }
        });

    }
    @Override
    public List<File> getAllFiles(){
        String sql = "SELECT * FROM MT950_FILEFORMATTER";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<File> result = new ArrayList<File>();
        for(Map<String, Object> row:rows){
            File emp = new File();
            emp.setId(Long.parseLong((String)row.get("Id")));
            emp.setFile_name((String)row.get("file_name"));
         //   emp.setFile_content(String.valueOf(row.get("file_content")));
            emp.setInputter_name((String)row.get("inputter_name"));
//            emp.setInputted_date((row.get("inputted_date"))));
            result.add(emp);

        }

        return result;
    }

    @Override
    public File getFileById(String hrefId) {
        String sql = "SELECT * FROM Formatted WHERE hrefId = ?";
        return (File)getJdbcTemplate().queryForObject(sql, new Object[]{hrefId}, new RowMapper<File>(){
            @Override
            public File mapRow(ResultSet rs, int rwNumber) throws SQLException {
                File emp = new File();
                emp.setId(Long.parseLong(rs.getString("id")));
                emp.setFile_name(rs.getString("file_name"));
                emp.setInputter_name(rs.getString("inputter_name"));
                return emp;
            }
        });
    }
}