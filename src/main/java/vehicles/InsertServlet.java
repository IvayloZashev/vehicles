package vehicles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(value = "/register")
public class InsertServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = new ConnectDB().getConnection();

        String sqlInsert = "insert into Vehicle (reg_number, model, color, year_manufacturing, owner) values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.setString(1, req.getParameter("reg_number"));
            preparedStatement.setString(2, req.getParameter("model"));
            preparedStatement.setString(3, req.getParameter("color"));

            preparedStatement.setInt(4, Integer.parseInt(req.getParameter("year")));
            preparedStatement.setString(5, req.getParameter("owner"));
            if (checkVehicleExist(req.getParameter("reg_number")) == null) {
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    resp.sendRedirect("success.jsp");
                } else {
                    resp.sendRedirect("error.jsp");
                }
            } else {
                resp.sendRedirect("alreadyExists.jsp");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vehicle checkVehicleExist(String regNumber) {
        String sqlSelect = "select * from Vehicle where reg_number = ?";
        Connection connection = new ConnectDB().getConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Vehicle vehicle = null;
        try {
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1, regNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                vehicle = new Vehicle();
                vehicle.setRegNumber(resultSet.getString("reg_number"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setColor(resultSet.getString("color"));
                vehicle.setYear(resultSet.getInt("year_manufacturing"));
                vehicle.setOwner(resultSet.getString("owner"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }
}
