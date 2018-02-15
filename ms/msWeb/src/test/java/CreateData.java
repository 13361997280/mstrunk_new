import com.qianwang.util.lang.DateUtil;
import org.apache.http.client.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by chenghaijiang on 2017/5/11.
 */
public class CreateData {
    private Connection conn;
    @Before
    public void testConnect() throws IOException, ClassNotFoundException, SQLException {

        String driver = "com.mysql.jdbc.Driver";
        String user = "canal";
        String pwd = "canal";
        String url = "jdbc:mysql://192.168.14.107:3306/ms";
        Class.forName(driver);
        conn = DriverManager.getConnection(url,user,pwd);
    }
    @Test
    public void testQuery() throws SQLException {
        PreparedStatement st = conn.prepareStatement("select * from user_label");
        ResultSet rs = st.executeQuery();
        while(rs.next()) {
            System.out.println(rs.getInt("e_user_id")+"|"+rs.getInt("user_channel")+"|"+rs.getString("base_sex")+"|"+rs.getInt("base_age"));
        }
    }
    @Test
    public void testUpdate() throws SQLException {
        PreparedStatement st = conn.prepareStatement("update user_label set base_age=24 where e_user_id=1");
        st.execute();
    }
    @Test
    public void testDelete() throws SQLException {
        PreparedStatement st = conn.prepareStatement("delete from user_label");
        st.execute();
    }
    @Test
    public void testAdd() throws SQLException {
        PreparedStatement st1 = conn.prepareStatement(
                "insert into user_label(e_user_id,user_channel,base_sex,base_age,base_marriage,base_girl,base_son,base_bacholr,create_time,update_time) " +
                "values(?,?,?,?,?,?,?,?,?,?)");
        Random rand = new Random();
        Date date  = new Date();
        String currentDay = DateUtil.formatDate(date, DateUtil.SHORT_TIMESTAMP_PATTERN);
        String[] sex = new String[]{"w","m"};
        for(int i=0,n=400;i <n;i++) {
            st1.setInt(1, i+1 );
            st1.setInt(2, rand.nextInt(3)+1 );
            st1.setString(3, sex[rand.nextInt(2)]);
            st1.setInt(4, rand.nextInt(100)+1);
            st1.setInt(5, rand.nextInt(4)-1);
            st1.setInt(6, rand.nextInt(4)-1);
            st1.setInt(7, rand.nextInt(4)-1);
            st1.setInt(8, rand.nextInt(9)-1);
            st1.setString(9, currentDay);
            st1.setString(10, currentDay);
            st1.addBatch();
        }
        st1.executeBatch();
    }
    @Test
    public void testAdd1() throws SQLException {
        PreparedStatement st1 = conn.prepareStatement(
                "insert into user_label" +
                        "(e_user_id,user_channel,user_type,base_sex,base_age,base_marriage,base_child,base_bachlor,base_born_addr,base_mob_addr,base_regit_date,base_phone_brand," +
                        "base_phone_type,base_sys_latest,base_sys_ususal,base_brower_rate,base_lbs_live_xy,base_lbs_live_code,base_lbs_city,base_lbs_home_xy,base_lbs_home_code," +
                        "base_lbs_work_xy,base_lbs_work_code,buy_goods_prefer,buy_avg_order_pric,buy_order_count,buy_order_sum,buy_baobi_sum,buy_baoquan_sum,buy_coupon_sum," +
                        "buy_card_sum,buy_last_date,buy_last_amount,buy_refund_count,buy_return_goods,buy_payment_rate,buy_card_count,seller_is_certifer,seller_history_level," +
                        "selller_sales_month,seller_rate_quan,commut_product_share_60,commut_product_price_avg,online_collect_count,online_cart_count,online_applaud_count," +
                        "online_search_key,online_search_count,online_search_rate,online_active_score,base_time_net_1,base_time_net_2,base_sign_time,invest_mechant_sum," +
                        "invest_qb,invest_quan,invest_deposit,invest_valid_refund,invest_frozen,invest_admin_cost,invest_coupon,invest_output_rate,invest_input_rate," +
                        "invest_shippment_assurance,invest_assurance_buy,invest_is_data_ass,invest_account_assure,invest_is_bcard,invest_is_jingedun,invest_rate_45_all," +
                        "invest_rank,invest_period,leisure_film_30," +
                        "leisure_scene_30,leisure_travel_30,leisure_show_times,leisure_tv_follow_count,leisure_qdou_out,leisure_qdou_count,leisure_gift_count," +
                        "leisure_tv_fans_all,leisure_fans_add,leisure_tv_watch_count,commut_present_count,commut_present_count_90,user_type_0,user_type_1,user_type_2," +
                        "user_type_3,user_type_4,dt,update_time) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        Random rand = new Random();
        Date date  = new Date();
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        String[] sys = new String[]{"ios","android","other"};
        String[] invest = new String[]{"-2","-1","0","1"};
        String[] y = new String[]{"Y","N"};
        for(int i=0,n=4000;i <n;i++) {
            st1.setString(1,(i+1)+"");//e_user_id
            st1.setString(2,(rand.nextInt(3)+1)+"");//user_channel
            st1.setString(3,(rand.nextInt(4)+1)+"");//user_type
            st1.setInt(4,rand.nextInt(3));
            st1.setInt(5,rand.nextInt(120)+1);
            st1.setInt(6,rand.nextInt(4)-1);
            st1.setInt(7,rand.nextInt(5)-1);
            st1.setInt(8,rand.nextInt(9)-1);
            st1.setString(9,"361222");
            st1.setString(10,"1001100");
            st1.setString(11,"20170519");
            st1.setInt(12,rand.nextInt(6)-1);
            st1.setString(13,"note1");
            st1.setString(14,sys[rand.nextInt(3)]);
            st1.setString(15,sys[rand.nextInt(3)]);
            st1.setInt(16,rand.nextInt(101)-1);
            st1.setString(17,"["+rand.nextInt(181)+","+rand.nextInt(91)+"]");
            st1.setString(18,"1001100");
            st1.setString(19,"1001101");
            st1.setString(20,"["+rand.nextInt(181)+","+rand.nextInt(91)+"]");
            st1.setString(21,"1001100");
            st1.setString(22,"["+rand.nextInt(181)+","+rand.nextInt(91)+"]");
            st1.setString(23,"1001100");
            st1.setString(24,(rand.nextInt(4)+1)+"");
            st1.setBigDecimal(25,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setInt(26,rand.nextInt(181));
            st1.setBigDecimal(27,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setBigDecimal(28,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setBigDecimal(29,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setBigDecimal(30,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setBigDecimal(31,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setString(32,"20170519");
            st1.setBigDecimal(33,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setInt(34,rand.nextInt(181));
            st1.setInt(35,rand.nextInt(181));
            st1.setBigDecimal(36,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setInt(37,rand.nextInt(101)+1);
            st1.setString(38,y[rand.nextInt(2)]);
            st1.setInt(39,rand.nextInt(5)+1);
            st1.setInt(40,rand.nextInt(10000));
            st1.setInt(41,rand.nextInt(101)+1);
            st1.setInt(42,rand.nextInt(10000));
            st1.setBigDecimal(43,new BigDecimal(dcmFmt.format(rand.nextFloat() * 1000)));
            st1.setInt(44,rand.nextInt(10000));
            st1.setInt(45,rand.nextInt(10000));
            st1.setInt(46,rand.nextInt(10000));
            st1.setString(47,"手机，短裙，手机壳，蜂蜜，电风扇");
            st1.setInt(48,rand.nextInt(10000));
            st1.setInt(49,rand.nextInt(101)+1);
            st1.setInt(50,rand.nextInt(10000));
            st1.setInt(51,rand.nextInt(24)+1);
            st1.setInt(52,rand.nextInt(24)+1);
            st1.setInt(53,rand.nextInt(24)+1);
            st1.setInt(54,rand.nextInt(10000));
            st1.setInt(55,rand.nextInt(10000));
            st1.setInt(56,rand.nextInt(10000));
            st1.setInt(57,rand.nextInt(10000));
            st1.setInt(58,rand.nextInt(10000));
            st1.setInt(59,rand.nextInt(10000));
            st1.setInt(60,rand.nextInt(10000));
            st1.setInt(61,rand.nextInt(101));
            st1.setInt(62,rand.nextInt(101));
            st1.setInt(63,rand.nextInt(101));
            st1.setString(64,y[rand.nextInt(2)]);
            st1.setString(65,y[rand.nextInt(2)]);
            st1.setString(66,y[rand.nextInt(2)]);
            st1.setString(67,y[rand.nextInt(2)]);
            st1.setString(68,y[rand.nextInt(2)]);
            st1.setString(69,y[rand.nextInt(2)]);
            st1.setInt(70,rand.nextInt(101));
            st1.setInt(71,rand.nextInt(5)+1);
            st1.setString(72,invest[rand.nextInt(4)]);
            st1.setInt(73,rand.nextInt(10000));
            st1.setInt(74,rand.nextInt(10000));
            st1.setInt(75,rand.nextInt(10000));
            st1.setInt(76,rand.nextInt(10000));
            st1.setInt(77,rand.nextInt(10000));
            st1.setInt(78,rand.nextInt(10000));
            st1.setInt(79,rand.nextInt(10000));
            st1.setInt(80,rand.nextInt(10000));
            st1.setInt(81,rand.nextInt(10000));
            st1.setInt(82,rand.nextInt(10000));
            st1.setInt(83,rand.nextInt(10000));
            st1.setInt(84,rand.nextInt(10000));
            st1.setInt(85,rand.nextInt(10000));
            st1.setInt(86,rand.nextInt(10000));
            st1.setInt(87,rand.nextInt(10000));
            st1.setInt(88,rand.nextInt(10000));
            st1.setInt(89,rand.nextInt(10000));
            st1.setInt(90,rand.nextInt(10000));
            st1.setString(91,"20170519");
            st1.setDate(92,new java.sql.Date(System.currentTimeMillis()));
            st1.addBatch();
        }
        st1.executeBatch();
    }
    public static void main(String[] args){
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        Random rand = new Random();
        float f = rand.nextFloat() * 1000;
        String a = dcmFmt.format(rand.nextFloat() * 1000);
        System.out.println(a);
        System.out.println(new BigDecimal(a));
    }
}
