
package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

/**
 *
 * @author jasom
 */
public class SellerDaoJDBC implements SellerDao{
    private Connection conn;
    
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public void insert(Seller obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Seller obj) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery(); //Recebe o resultado em forma de tabela, usa comandos da API JDBC para percorrer a tabela
            
            //comando next() retorna um false se não houver dados na linha
            if(rs.next()){ 
                Department dep = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs, dep);
                
                return obj;
            }
            return null;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        
        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();       
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);
        return obj;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE Department.Id = ? ORDER BY Name");
            st.setInt(1, department.getId());
            rs = st.executeQuery(); //Recebe o resultado em forma de tabela, usa comandos da API JDBC para percorrer a tabela
            
            List <Seller> list = new ArrayList<>(); //List para receber o venderor ou vendedores resultado da pesquisa na tabela
            Map<Integer, Department> map = new HashMap<>(); //Crio um map para armazenar os departamentos, pois os objetos não podem apontar para departamentos diferentes
            
            /*
            RESTRIÇÃO: NÃO POSSO SAIR CRIANDO VÁRIOS DEPARTAMENTOS E FAZER MEUS VENDEDORES (OBJ) APONTAREM PARA APARTAMENTOS DIFERENTES INSTACIADOS NA MEMORIA
            POR ISSO É NECESSÁRIO CRIAR UM ED MAP PARA RECEBER OS ids DAS INSTANCIAS EXISTENTES.
            ESTAMOS RETIRANDO INFORMAÇÕES DO BANCO DE DADOS, E LÁ SÓ EXISTE 1 DEPARTAMENTO POR ID. LEMBRE-SE.
            */
            
            //comando next() retorna um false se não houver dados na linha
            while(rs.next()){ 
                Department dep = map.get(rs.getInt("DepartmentId")); //Atribuo o resultado do map a variavel dep
            
                if(dep == null){ //caso não exista esse departamento, ele retorna null e entra nesse if
                dep = instantiateDepartment(rs); //atribuo o valor retornado pelo função que instancia os departamentos
                map.put(rs.getInt("DepartmentId"), dep); //incluo o id do departamento do meu departamento no meu map, pra da proxima vez ele pular o if
                }
                
                Seller obj = instantiateSeller(rs, dep); //instancio o vendedor apontando para o meu dep
                list.add(obj); //adciono meu vendedor ao list
            }
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
}
