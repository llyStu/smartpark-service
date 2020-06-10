package com.vibe.service.ptrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.ptrl.PtrlConsequenceMapper;
import com.vibe.pojo.mounting.ConnMountiong;
import com.vibe.pojo.ptrl.PtrlRecord;
import com.vibe.utils.Page;


@Service
public class PtrlConsequenceServiceimpl implements PtrlConsequenceService {
    private final static String CONNNAME = "巡更连接";

    @Autowired
    private PtrlConsequenceMapper pcm;

    @Override
    public Page<PtrlRecord> patrollingRecord(int pageNum, int pageSize) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        List<PtrlRecord> ptrlRecords = null;
        try {
            ConnMountiong cm = pcm.findOneConn(CONNNAME);//获取该类型的连接配置
            if (null == cm.getActuator() || null == cm.getDburl() || null == cm.getUsername() || null == cm.getPassword()) {
                System.out.println(CONNNAME + "未获取到jdbc驱动配置");
                return null;
            }
            conn = getConn(cm.getActuator(), cm.getDburl(), cm.getUsername(), cm.getPassword());
            if (null == conn) return null;
            ptrlRecords = new ArrayList<PtrlRecord>();//创建存储容器
            //System.out.println("连接成功");
            statement = conn.createStatement();
            rs = statement.executeQuery("select "
                    + "poc.sn,poc.scheduleid,poc.PosSn,poc.ptrltimeSta,poc.flag,poc.ptrltimeEnd,poc.ptrltruetime,poc.checkFlag,poc.lineid,"
                    + "pos.ptrldate,pos.ptrlman,pops.name,pol.linename,popl.name as manname from "
                    + "ptrl_off_Check poc inner join ptrl_off_schedule pos on poc.scheduleid=pos.scheduleid"
                    + " inner join ptrl_off_positions pops on poc.PosSn=pops.sn "
                    + "inner join ptrl_off_line pol on poc.lineid=pol.lineid inner join ptrl_off_ptrlmans popl on pos.ptrlman=popl.id ");
            while (rs.next()) {
                PtrlRecord ptrlRecord = new PtrlRecord();
                ptrlRecord.setSn(rs.getInt("sn"));
                ptrlRecord.setScheduleid(rs.getInt("scheduleid"));
                ptrlRecord.setPosSn(rs.getInt("PosSn"));
                ptrlRecord.setPtrltimeSta(rs.getString("ptrltimeSta"));
                ptrlRecord.setFlag(rs.getString("flag"));
                ptrlRecord.setPtrltimeEnd(rs.getString("ptrltimeEnd"));
                ptrlRecord.setPtrltruetime(rs.getString("ptrltruetime"));
                ptrlRecord.setCheckFlag(rs.getString("checkFlag"));
                ptrlRecord.setLineid(rs.getInt("lineid"));
                ptrlRecord.setPtrldate(rs.getString("ptrldate"));
                ptrlRecord.setPtrlman(rs.getInt("ptrlman"));
                ptrlRecord.setName(rs.getString("name"));
                ptrlRecord.setLinename(rs.getString("linename"));
                ptrlRecord.setManname(rs.getString("manname"));
                //	System.out.println(ptrlRecord.toString());
                ptrlRecords.add(ptrlRecord);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (statement != null) try {
                statement.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        return toPage(ptrlRecords);
    }

    private Connection getConn(String driver, String dburl, String username, String password) {
        Connection dbConn = null;
        try {
            // 加载驱动
            Class.forName(driver);
            // 获取数据库连接
            dbConn = DriverManager.getConnection(dburl, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConn;
    }

    private static <T> Page<T> toPage(List<T> list) {
        PageInfo<T> page = new PageInfo<>(list);
        Page<T> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }
}
