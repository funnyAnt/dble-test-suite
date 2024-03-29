package actiontech.dble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobSample extends InterfaceTest{

	public ClobSample(ConnProperties mysqlProp, ConnProperties uproxyProp) throws SQLException {
		super(mysqlProp, uproxyProp);
	}

	protected void start()throws SQLException {
		createTable();
		populateTable();
		addRowToCoffeeDescriptions("Colombian", "txt/colombian-description.txt");
		retrieveExcerpt("Colombian", 10);
		System.out.println("pass! Clob getClob(), setClob() !");
	}

	/**
	 * getClob()
	 */
	public void retrieveExcerpt(String coffeeName,
			int numChar) throws SQLException {

		Clob mysql_clob = null,uproxy_clob=null;
		PreparedStatement mysql_pstmt = null, uproxy_pstmt=null;

		try {
			String sql = "select COF_DESC from COFFEE_DESCRIPTIONS " + "where COF_NAME = ?";
			mysql_pstmt = this.mysqlConn.prepareStatement(sql);
			mysql_pstmt.setString(1, coffeeName);
			ResultSet mysql_rs = mysql_pstmt.executeQuery();
			uproxy_pstmt = this.uproxyConn.prepareStatement(sql);
			uproxy_pstmt.setString(1, coffeeName);
			ResultSet uproxy_rs = uproxy_pstmt.executeQuery();
			if (mysql_rs.next()) {
				uproxy_rs.next();
				mysql_clob = mysql_rs.getClob(1);
				uproxy_clob = uproxy_rs.getClob(1);
				String mysql_str = mysql_clob.getSubString(1, (int)mysql_clob.length());
				String uproxy_str = uproxy_clob.getSubString(1, (int)uproxy_clob.length());
				if(!mysql_str.equals(uproxy_str)){
					print_debug("mysql:"+mysql_str);
					print_debug("uproxy:"+uproxy_str);
					on_assert_fail("fail! after insert blob, the value is different.");
				}
			}
		} catch (SQLException sqlex) {
			TestUtilities.printSQLException(sqlex);
		} catch (Exception ex) {
			System.out.println("Unexpected exception: " + ex.toString());
			on_assert_fail("fail!");
		} finally {
			if (mysql_pstmt != null) mysql_pstmt.close();
			if (uproxy_pstmt != null) uproxy_pstmt.close();
		}
	}


	/**
	 * setClob()
	 * Connection#Clob	createClob()
	 */
	public void addRowToCoffeeDescriptions(String coffeeName,
			String fileName) throws SQLException {
		PreparedStatement mysql_pstmt = null, uproxy_pstmt = null;
		try {
			Clob myClob = this.mysqlConn.createClob();

			Writer clobWriter = myClob.setCharacterStream(1);
			String str = this.readFile(fileName, clobWriter);
			print_debug("Wrote the following: " + clobWriter.toString());
			print_debug("MySQL, setting String in Clob object with setString method");
			myClob.setString(1, str);
			myClob.setString(1, str);
			print_debug("Length of Clob: " + myClob.length());
			String sql = "INSERT INTO COFFEE_DESCRIPTIONS VALUES(?,?)";
			mysql_pstmt = this.mysqlConn.prepareStatement(sql);
			mysql_pstmt.setString(1, coffeeName);
			mysql_pstmt.setClob(2, myClob);
			mysql_pstmt.executeUpdate();

			uproxy_pstmt = this.uproxyConn.prepareStatement(sql);
			uproxy_pstmt.setString(1, coffeeName);
			uproxy_pstmt.setClob(2, myClob);
			uproxy_pstmt.executeUpdate();
		} catch (SQLException sqlex) {
			TestUtilities.printSQLException(sqlex);
		} catch (Exception ex) {
			System.out.println("Unexpected exception: " + ex.toString());
		} finally {
			if (mysql_pstmt != null) { mysql_pstmt.close(); }
			if (uproxy_pstmt != null) { uproxy_pstmt.close(); }
		}
	}

	private String readFile(String fileName,
			Writer writerArg) throws FileNotFoundException,
	IOException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String nextLine = "";
		StringBuffer sb = new StringBuffer();
		while ((nextLine = br.readLine()) != null) {
			print_debug("Writing: " + nextLine);
			writerArg.write(nextLine);
			sb.append(nextLine);
		}
		// Convert the content into to a string
		String clobData = sb.toString();

		br.close();
		// Return the data.
		return clobData;
	}

}
