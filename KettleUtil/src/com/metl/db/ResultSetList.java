/**
* Project Name:KettleUtil
* Date:2016年6月21日上午12:16:28
* Copyright (c) 2016, jingma@iflytek.com All Rights Reserved.
*/

package com.metl.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取对象列表 <br/>
 * date: 2016年6月21日 上午12:16:28 <br/>
 * @author jingma@iflytek.com
 * @version 
 */
public class ResultSetList implements ResultSetExtractor {

    /**
     * 
     * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
     */
    @Override
    public Object extractData(ResultSet rs) throws SQLException,
            DataAccessException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        JSONObject json = null;
        ResultSetMetaData metadata = rs.getMetaData();
        while(rs.next()){
            json = new JSONObject();
            for(int i=1;i<=metadata.getColumnCount();i++){
                String colName = metadata.getColumnName(i).toLowerCase();
                json.put(colName, rs.getObject(i));
            }
            list.add(json);
        }
        return list;
    }

}
