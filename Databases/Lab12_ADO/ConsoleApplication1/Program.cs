using System;

using System.Data.SqlClient;
using System.Data;
using System.Configuration;

namespace ConsoleApplication1
{
    class HospitalDB
    {

        private static SqlConnection connection;

        private static DataSet dataset;
        private static SqlDataAdapter data_adapter;


        static public void connectToDB()
        {
            string connection_string = ConfigurationManager.ConnectionStrings["HospitalVisits"].ConnectionString;
            connection = new SqlConnection(connection_string);
            try
            {
                connection.Open();
                Console.WriteLine("Connection opened!");
            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
        }

        static public bool isConnect()
        {
            return connection != null && connection.State != ConnectionState.Closed;
        }

        static public void disconnectToDB()
        {
            if (isConnect())
            {
                try
                {
                    connection.Close();
                    Console.WriteLine("Connection closed!");
                }
                catch (Exception ex)
                {
                    Console.Write(ex.Message);
                    Console.ReadKey();
                    Environment.Exit(-1);
                }
            }
        }

        static public void ShowDataConnectedLayer(string table_name)
        {
            try
            {
                Console.WriteLine("ShowDataConnectedLayer(" + table_name + ")");
                SqlCommand cmd = connection.CreateCommand();
                cmd.Connection = connection;
                cmd.CommandText = "SELECT * FROM " + table_name;

                SqlDataReader reader = cmd.ExecuteReader();

                for (int i = 0; i < reader.FieldCount; i++)
                {
                    Console.Write(reader.GetName(i) + "\t");
                }
                Console.WriteLine();
                while (reader.Read())
                {
                    for (int i = 0; i < reader.FieldCount; i++)
                    {
                        Console.Write(reader.GetValue(i) + "\t");
                    }
                    Console.WriteLine();
                }

                reader.Close();

            }
            catch (SqlException ex)
            {
                Console.Write(ex.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
        }


        static public void deleteVisitsConnectedLayer(DateTime before, DateTime after)
        {
            try
            {
                SqlCommand cmd = connection.CreateCommand();
                cmd.Connection = connection;
                cmd.CommandText = "DELETE FROM Visit WHERE visit_date BETWEEN @before_date AND @after_date";

                SqlParameter[] parametes = new SqlParameter[2];
                parametes[0] = new SqlParameter();
                parametes[0].ParameterName = "@before_date";
                parametes[0].Value = before;
                parametes[0].SqlDbType = SqlDbType.DateTime;

                parametes[1] = new SqlParameter();
                parametes[1].ParameterName = "after_date";
                parametes[1].Value = after;
                parametes[1].SqlDbType = SqlDbType.DateTime;

                cmd.Parameters.AddRange(parametes);
                cmd.ExecuteNonQuery();

            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
        }

        static public void insertVisitConnectedLayer(DateTime visit_date, TimeSpan visit_time, Int64 patient_id)
        {
            try
            {
                SqlCommand cmdcheckPatient = connection.CreateCommand();
                cmdcheckPatient.Connection = connection;
                cmdcheckPatient.CommandText = "SELECT Name FROM Patient WHERE patient_id = @patient_id";

                SqlParameter param = new SqlParameter();
                param.ParameterName = "@patient_id";
                param.Value = patient_id;
                param.SqlDbType = SqlDbType.Int;

                cmdcheckPatient.Parameters.Add(param);

                string name = (string)cmdcheckPatient.ExecuteScalar();
                if (name == null)
                {
                    Console.WriteLine("Добавление визита к доктору от несуществующего ID пациента!");
                    return;
                }


                SqlCommand cmd = connection.CreateCommand();
                cmd.Connection = connection;
                cmd.CommandText = "INSERT INTO Visit(visit_date,visit_time,patient_id) VALUES (@visit_date,@visit_time,@patient_id)";

                SqlParameter[] parametes = new SqlParameter[3];
                parametes[0] = new SqlParameter();
                parametes[0].ParameterName = "@visit_date";
                parametes[0].Value = visit_date;
                parametes[0].SqlDbType = SqlDbType.DateTime;

                parametes[1] = new SqlParameter();
                parametes[1].ParameterName = "@visit_time";
                parametes[1].Value = visit_time;
                parametes[1].SqlDbType = SqlDbType.Time;

                parametes[2] = new SqlParameter();
                parametes[2].ParameterName = "@patient_id";
                parametes[2].Value = patient_id;
                parametes[2].SqlDbType = SqlDbType.Int;

                cmd.Parameters.AddRange(parametes);
                cmd.ExecuteNonQuery();

            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
        }

        static public void updateVisitConnectedLayer(DateTime visit_date, TimeSpan visit_time, Int64 patient_id, Int64 visit_id)
        {
            try
            {
                SqlCommand cmdcheckPatient = connection.CreateCommand();
                cmdcheckPatient.Connection = connection;
                cmdcheckPatient.CommandText = "SELECT Name FROM Patient WHERE patient_id = @patient_id";

                SqlParameter param = new SqlParameter();
                param.ParameterName = "@patient_id";
                param.Value = patient_id;
                param.SqlDbType = SqlDbType.Int;

                cmdcheckPatient.Parameters.Add(param);

                string name = (string)cmdcheckPatient.ExecuteScalar();
                if (name == null)
                {
                    Console.WriteLine("Нельзя обновить визит! Ошибка с ID пациента, так как не существует.");
                    return;
                }


                SqlCommand cmd = connection.CreateCommand();
                cmd.Connection = connection;
                cmd.CommandText = "UPDATE Visit SET visit_date = @visit_date, visit_time = @visit_time WHERE visit_id = @visit_id";

                SqlParameter[] parametes = new SqlParameter[4];
                parametes[0] = new SqlParameter();
                parametes[0].ParameterName = "@visit_date";
                parametes[0].Value = visit_date;
                parametes[0].SqlDbType = SqlDbType.DateTime;

                parametes[1] = new SqlParameter();
                parametes[1].ParameterName = "@visit_time";
                parametes[1].Value = visit_time;
                parametes[1].SqlDbType = SqlDbType.Time;

                parametes[2] = new SqlParameter();
                parametes[2].ParameterName = "@patient_id";
                parametes[2].Value = patient_id;
                parametes[2].SqlDbType = SqlDbType.Int;

                parametes[3] = new SqlParameter();
                parametes[3].ParameterName = "@visit_id";
                parametes[3].Value = visit_id;
                parametes[3].SqlDbType = SqlDbType.Int;

                cmd.Parameters.AddRange(parametes);
                cmd.ExecuteNonQuery();

            }
            catch (Exception ex)
            {
                Console.Write(ex.Message);
                Console.ReadKey();
                Environment.Exit(-1);
            }
        }

        static public void configDisconnectedLayer()
        {
            dataset = new DataSet();
            data_adapter = new SqlDataAdapter("SELECT * FROM Visit", connection);
            data_adapter.Fill(dataset, "Visits");
            data_adapter = new SqlDataAdapter("SELECT * FROM Patient", connection);
            data_adapter.Fill(dataset, "Patient");

            ForeignKeyConstraint FK_Patient = new ForeignKeyConstraint("FK_Patient",
                                                                        dataset.Tables["Patient"].Columns["patient_id"],
                                                                        dataset.Tables["Visits"].Columns["patient_id"]);
            FK_Patient.DeleteRule = Rule.Cascade;
            FK_Patient.UpdateRule = Rule.Cascade;
            dataset.Tables["Visits"].Constraints.Add(FK_Patient);
        }

        static public void ShowDataDisconnectedLayer(string table_name)
        {
            Console.WriteLine("ShowDataDisconnectedLayer(" + table_name + ")");
            ShowDataConnectedLayer(table_name);
        }


        static public void deleteVisitsDisconnectedLayer(DateTime before, DateTime after)
        {
            SqlCommand cmd = new SqlCommand();
            cmd.Connection = connection;
            cmd.CommandText = "DELETE FROM Visit WHERE visit_date BETWEEN @before_date AND @after_date";

            cmd.Parameters.Add("@before_date", SqlDbType.DateTime, 0, "visit_date");
            cmd.Parameters.Add("@after_date", SqlDbType.DateTime, 0, "visit_date");

            data_adapter.DeleteCommand = cmd;

            for (int i = 0; i < dataset.Tables["Visits"].Rows.Count; i++)
            {
                DataRow row = dataset.Tables["Visits"].Rows[i];
                if ((DateTime)row["visit_date"] > before && (DateTime)row["visit_date"] < after)
                {
                    row.Delete();
                }
            }
            data_adapter.Update(dataset, "Visits");
        }

        static public void insertVisitDisconnectedLayer(DateTime visit_date, TimeSpan visit_time, Int64 patient_id)
        {

            SqlCommand cmd = connection.CreateCommand();
            cmd.Connection = connection;
            cmd.CommandText = "INSERT INTO Visit(visit_date,visit_time,patient_id) VALUES (@visit_date,@visit_time,@patient_id); set @id = SCOPE_IDENTITY()";

            cmd.Parameters.Add("@visit_date", SqlDbType.DateTime, 0, "visit_date");
            cmd.Parameters.Add("@visit_time", SqlDbType.Time, 0, "visit_time");
            cmd.Parameters.Add("@patient_id", SqlDbType.Int, 0, "patient_id");


            data_adapter.InsertCommand = cmd;

            DataRow dataRow = dataset.Tables["Visits"].NewRow();
            dataRow["visit_date"] = visit_date;
            dataRow["visit_time"] = visit_time;
            dataRow["patient_id"] = patient_id;

            dataset.Tables["Visits"].Rows.Add(dataRow);
            data_adapter.Update(dataset, "Visits");

        }

        static public void updateVisitDisconnectedLayer(DateTime visit_date, TimeSpan visit_time, Int64 patient_id, Int64 visit_id)
        {
            SqlCommand cmd = connection.CreateCommand();
            cmd.Connection = connection;
            cmd.CommandText = "UPDATE Visit SET visit_date = @visit_date, visit_time = @visit_time WHERE visit_id = @visit_id";

            cmd.Parameters.Add("@visit_date", SqlDbType.DateTime, 0, "visit_date");
            cmd.Parameters.Add("@visit_time", SqlDbType.Time, 0, "visit_time");
            cmd.Parameters.Add("@patient_id", SqlDbType.Int, 0, "patient_id");
            cmd.Parameters.Add("@visit_id", SqlDbType.Int, 0, "visit_id");

            cmd.Parameters["@visit_id"].SourceVersion = DataRowVersion.Original;

            data_adapter.UpdateCommand = cmd;

            for (int i = 0; i < dataset.Tables["Visits"].Rows.Count; i++)
            {
                DataRow row = dataset.Tables["Visits"].Rows[i];
                Console.WriteLine("\n\n\n\n\n\n\n\n" + row["visit_id"]);
                if (row["visit_id"] != DBNull.Value && (Int64)row["visit_id"] == visit_id)
                {
                    row["visit_date"] = visit_date;
                    row["visit_time"] = visit_time;
                    row["patient_id"] = patient_id;
                }
            }

            data_adapter.Update(dataset, "Visits");

       }



    }
    class Program
    {
        static void Main(string[] args)
        {
            HospitalDB.connectToDB();
            Console.WriteLine(HospitalDB.isConnect());
            // Связный уровень //
            HospitalDB.ShowDataConnectedLayer("Visit");
            HospitalDB.insertVisitConnectedLayer(new DateTime(2016, 05, 25), new TimeSpan(11, 50, 00), 3);
            HospitalDB.insertVisitConnectedLayer(new DateTime(2016, 12, 03), new TimeSpan(13, 20, 00), 1);
            HospitalDB.ShowDataConnectedLayer("Visit");
            HospitalDB.updateVisitConnectedLayer(new DateTime(2016, 10, 06), new TimeSpan(12, 20, 00), 3, 5);
            HospitalDB.ShowDataConnectedLayer("Visit");
            HospitalDB.deleteVisitsConnectedLayer(new DateTime(2016, 10, 06), new DateTime(2016, 12, 31));
            HospitalDB.ShowDataConnectedLayer("Visit");
            // Несвязный уровень //
            Console.WriteLine("Disconnected Layer");
            HospitalDB.configDisconnectedLayer();
            HospitalDB.insertVisitDisconnectedLayer(new DateTime(2016, 04, 12), new TimeSpan(16, 00, 00), 4);
            HospitalDB.ShowDataDisconnectedLayer("Visit");

            /* HospitalDB.updateVisitDisconnectedLayer(new DateTime(2016, 03, 19), new TimeSpan(10, 00, 00), 6, 2);
            HospitalDB.ShowDataDisconnectedLayer("Visit"); */
            HospitalDB.deleteVisitsDisconnectedLayer(new DateTime(2016, 01, 01), new DateTime(2016, 12, 31));
            HospitalDB.ShowDataConnectedLayer("Visit");
            HospitalDB.disconnectToDB();
            Console.ReadKey();
        }
    }
}
