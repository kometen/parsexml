// Definere et scala-projekt med sbt.
// https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html

// Kør med sbt run

// https://github.com/databricks/spark-xml

import org.apache.spark.sql.SparkSession
import com.databricks.spark.xml._


object WordCount {
	def main(args: Array[String]) {
		// https://stackoverflow.com/questions/38008330/spark-error-a-master-url-must-be-set-in-your-configuration-when-submitting-a#40555616
		val spark = SparkSession
			.builder
			.appName("Databricks XML parsing")
			.config("spark.master", "local")
			.getOrCreate()

		val df = spark.read
			.option("rootTag", "RAPPORT")
			.option("rowTag", "NOARKSAK.OJ")
			.xml("/Users/claus/data/spark/20160229-20160306_utf8.xml")

		df.printSchema()

		val selectedData = df.select(
			"`JOURNPOST.OJ`.`AVSMOT.OJ`.`AM.IHTYPE`",
			"`JOURNPOST.OJ`.`AVSMOT.OJ`.`AM.NAVN`",
			"`JOURNPOST.OJ`.`JP.DOKDATO`",
			"`JOURNPOST.OJ`.`JP.JAAR`",
			"`JOURNPOST.OJ`.`JP.JDATO`",
			"`JOURNPOST.OJ`.`JP.NDOKTYPE`",
			"`JOURNPOST.OJ`.`JP.OFFINNHOLD`",
			"`JOURNPOST.OJ`.`JP.POSTNR`",
			"`JOURNPOST.OJ`.`JP.SEKNR`",
			"`JOURNPOST.OJ`.`JP.TGKODE`",
			"`JOURNPOST.OJ`.`JP.UOFF`",
			"`KLASSERING.OJ`.`KL.OPLTEKST`",
			"`KLASSERING.OJ`.`KL.ORDNVERDI`",
			"`KLASSERING.OJ`.`KL.OVBESK`",
			"`KLASSERING.OJ`.`KL.SORT`",
			"`SA.ADMKORT`",
			"`SA.ANSVINIT`",
			"`SA.OFFTITTEL`",
			"`SA.SAAR`",
			"`SA.SEKNR`"
		)

		selectedData.select("`AM.NAVN`").show(2, false)
		selectedData.show(3, false)

		selectedData.write.parquet("journal.parquet")

		spark.stop
	}
}
