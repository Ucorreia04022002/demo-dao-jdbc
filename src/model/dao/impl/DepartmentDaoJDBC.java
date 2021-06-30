package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements  DepartmentDao{

	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(
				"INSERT INTO department " +
				"(Name) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			conn.commit();
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Insert rolled back! caused by: "+ e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to roll back! caused by: "+ e1.getMessage());
			}
		} 
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(
					"UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");	
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			st.executeUpdate();
			conn.commit();
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Update rolled back! caused by: "+ e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to roll back! caused by: "+ e1.getMessage());
			}
		} 
		finally {
			DB.closeStatement(st);
			
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
			st.setInt(1, id);		
			int rows = st.executeUpdate();
			if (rows == 0) {
				throw new DbException("non existent id selected");
			}
			conn.commit();
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Delete rolled back! caused by: "+ e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to roll back! caused by: "+ e1.getMessage());
			}
		} 
		finally {
			DB.closeStatement(st);
		}
	}
		
	

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(
				"SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				return obj;
			}
			conn.commit();
			return null;
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("findById rolled back! caused by: "+ e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to roll back! caused by: "+ e1.getMessage());
			}
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(
				"SELECT * FROM department ORDER BY Name");
			rs = st.executeQuery();

			List<Department> list = new ArrayList<>();

			while (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				list.add(obj);
			}
			conn.commit();
			return list;
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("findAll rolled back! caused by: "+ e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to roll back! caused by: "+ e1.getMessage());
			}
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	
}
