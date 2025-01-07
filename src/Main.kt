import java.sql.*
import java.util.Scanner

const val DB_URL = "jdbc:mysql://localhost:3306/handson_13"
const val DB_USER = "root"
const val DB_PASSWORD = ""

val kamarDefault = mapOf(
    "1" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "2" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "3" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "4" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "5" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "6" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "7" to mapOf("tipe" to "Biasa", "harga" to "98000"),
    "8" to mapOf("tipe" to "Reguler", "harga" to "117000"),
    "9" to mapOf("tipe" to "Reguler", "harga" to "117000"),
    "10" to mapOf("tipe" to "Reguler", "harga" to "117000"),
    "11" to mapOf("tipe" to "Reguler", "harga" to "117000"),
    "12" to mapOf("tipe" to "Reguler", "harga" to "117000"),
    "13" to mapOf("tipe" to "Reguler", "harga" to "117000"),
    "14" to mapOf("tipe" to "VIP", "harga" to "198000"),
    "15" to mapOf("tipe" to "VIP", "harga" to "198000"),
    "16" to mapOf("tipe" to "VIP", "harga" to "198000"),
    "17" to mapOf("tipe" to "VIP", "harga" to "198000"),
    "18" to mapOf("tipe" to "VIP", "harga" to "198000")
)

fun main() {
    val scanner = Scanner(System.`in`)
    var connection: Connection? = null

    try {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
        println("Koneksi berhasil dengan database!")
    } catch (e: SQLException) {
        e.printStackTrace()
        return
    }

    if (!login(scanner, connection)) {
        println("Autentikasi gagal. Aplikasi ditutup.")
        return
    }

    while (true) {
        println("\n--= Menu Penginapan Risni =--")
        println("1. Manajemen Kamar")
        println("2. Manajemen Reservasi")
        println("3. Cek Status Kamar")
        println("4. Rekap Penginapan")
        println("5. Keluar")
        print("Pilih menu: ")

        when (scanner.nextLine()) {
            "1" -> menuManajemenKamar(scanner, connection)
            "2" -> menuManajemenReservasi(scanner, connection)
            "3" -> cekStatusKamar(connection)
            "4" -> rekapPenginapan(connection)
            "5" -> {
                println("Terima kasih telah menggunakan aplikasi penginapan Risni!")
                break
            }
            else -> println("Pilihan tidak valid. Coba lagi.")
        }
    }

    connection.close()
}

fun login(scanner: Scanner, connection: Connection): Boolean {
    println("=== Selamat datang di aplikasi penginapan Risni ===")
    print("Masukkan username: ")
    val username = scanner.nextLine()
    print("Masukkan password: ")
    val password = scanner.nextLine()

    val query = "SELECT * FROM admin WHERE username = ? AND password = ?"
    val preparedStatement = connection.prepareStatement(query)
    preparedStatement.setString(1, username)
    preparedStatement.setString(2, password)
    val resultSet = preparedStatement.executeQuery()

    return if (resultSet.next()) {
        println("Login berhasil. Selamat datang, Admin!")
        true
    } else {
        println("Username atau password salah!")
        false
    }
}

fun menuManajemenKamar(scanner: Scanner, connection: Connection) {
    while (true) {
        println("\n--= Manajemen Kamar =--")
        println("1. Tambah Kamar")
        println("2. Lihat Daftar Kamar")
        println("3. Hapus Kamar")
        println("4. Kembali")
        print("Pilih menu: ")

        when (scanner.nextLine()) {
            "1" -> tambahKamar(scanner, connection)
            "2" -> lihatDaftarKamar(connection)
            "3" -> hapusKamar(scanner, connection)
            "4" -> return
            else -> println("Pilihan tidak valid. Coba lagi.")
        }
    }
}

fun tambahKamar(scanner: Scanner, connection: Connection) {
    println("===============================================")
    println("            List Kamar Penginapan Risni        ")
    println("===============================================")
    println("| No |      Tipe Kamar      |   Harga/Malam   |")
    println("===============================================")
    println("|  1 |        Biasa         |   Rp.98.000     |")
    println("|  2 |        Biasa         |   Rp.98.000     |")
    println("|  3 |        Biasa         |   Rp.98.000     |")
    println("|  4 |        Biasa         |   Rp.98.000     |")
    println("|  5 |        Biasa         |   Rp.98.000     |")
    println("|  6 |        Biasa         |   Rp.98.000     |")
    println("|  7 |        Biasa         |   Rp.98.000     |")
    println("|  8 |       Reguler        |  Rp.117.000     |")
    println("|  9 |       Reguler        |  Rp.117.000     |")
    println("| 10 |       Reguler        |  Rp.117.000     |")
    println("| 11 |       Reguler        |  Rp.117.000     |")
    println("| 12 |       Reguler        |  Rp.117.000     |")
    println("| 13 |       Reguler        |  Rp.117.000     |")
    println("| 14 |         VIP          |  Rp.198.000     |")
    println("| 15 |         VIP          |  Rp.198.000     |")
    println("| 16 |         VIP          |  Rp.198.000     |")
    println("| 17 |         VIP          |  Rp.198.000     |")
    println("| 18 |         VIP          |  Rp.198.000     |")
    println("===============================================")
    print("Masukkan nomor kamar: ")
    val nomor = scanner.nextLine()

    val dataKamar = kamarDefault[nomor]
    if (dataKamar != null) {
        val query = "INSERT INTO kamar (nomor, tipe, harga, status) VALUES (?, ?, ?, 'Tersedia')"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, nomor.toInt())
        preparedStatement.setString(2, dataKamar["tipe"])
        preparedStatement.setInt(3, dataKamar["harga"]!!.toInt())
        preparedStatement.executeUpdate()

        println("Kamar $nomor berhasil ditambahkan.")
    } else {
        println("Nomor kamar $nomor tidak ditemukan dalam data default. Penambahan gagal.")
    }
}

fun lihatDaftarKamar(connection: Connection) {
    val query = "SELECT * FROM kamar"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)

    println("\n--= Daftar Kamar =--")
    while (resultSet.next()) {
        val nomor = resultSet.getInt("nomor")
        val tipe = resultSet.getString("tipe")
        val harga = resultSet.getInt("harga")
        val status = resultSet.getString("status")
        println("==============================")
        println(" Nomor Kamar       : ${nomor} ")
        println(" Tipe Kamar        : ${tipe}  ")
        println(" Harga Permalam    : Rp${harga}")
        println(" Status            : ${status}")
        println("==============================")
        println()
    }
}

fun hapusKamar(scanner: Scanner, connection: Connection) {
    print("Masukkan nomor kamar yang ingin dihapus: ")
    val nomor = scanner.nextLine().toInt()

    val query = "DELETE FROM kamar WHERE nomor = ?"
    val statement = connection.prepareStatement(query)
    statement.setInt(1, nomor)

    val rowsDeleted = statement.executeUpdate()
    if (rowsDeleted > 0) {
        println("Kamar $nomor berhasil dihapus.")
    } else {
        println("Kamar tidak ditemukan.")
    }
}

fun menuManajemenReservasi(scanner: Scanner, connection: Connection) {
    while (true) {
        println("\n--= Manajemen Reservasi =--")
        println("1. Tambah Reservasi")
        println("2. Lihat Daftar Reservasi")
        println("3. Hapus Reservasi")
        println("4. Kembali")
        print("Pilih menu: ")

        when (scanner.nextLine()) {
            "1" -> tambahReservasi(scanner, connection)
            "2" -> lihatDaftarReservasi(connection)
            "3" -> hapusReservasi(scanner, connection)
            "4" -> return
            else -> println("Pilihan tidak valid. Coba lagi.")
        }
    }
}

fun tambahReservasi(scanner: Scanner, connection: Connection) {
    print("Masukkan nama tamu: ")
    val nama = scanner.nextLine()
    println("===============================================")
    println("            List Kamar Penginapan Risni        ")
    println("===============================================")
    println("| No |      Tipe Kamar      |   Harga/Malam   |")
    println("===============================================")
    println("|  1 |        Biasa         |   Rp.98.000     |")
    println("|  2 |        Biasa         |   Rp.98.000     |")
    println("|  3 |        Biasa         |   Rp.98.000     |")
    println("|  4 |        Biasa         |   Rp.98.000     |")
    println("|  5 |        Biasa         |   Rp.98.000     |")
    println("|  6 |        Biasa         |   Rp.98.000     |")
    println("|  7 |        Biasa         |   Rp.98.000     |")
    println("|  8 |       Reguler        |  Rp.117.000     |")
    println("|  9 |       Reguler        |  Rp.117.000     |")
    println("| 10 |       Reguler        |  Rp.117.000     |")
    println("| 11 |       Reguler        |  Rp.117.000     |")
    println("| 12 |       Reguler        |  Rp.117.000     |")
    println("| 13 |       Reguler        |  Rp.117.000     |")
    println("| 14 |         VIP          |  Rp.198.000     |")
    println("| 15 |         VIP          |  Rp.198.000     |")
    println("| 16 |         VIP          |  Rp.198.000     |")
    println("| 17 |         VIP          |  Rp.198.000     |")
    println("| 18 |         VIP          |  Rp.198.000     |")
    println("===============================================")
    print("Masukkan nomor kamar: ")
    val nomorKamar = scanner.nextLine().toInt()
    print("Masukkan tanggal check-in (YYYY-MM-DD): ")
    val checkIn = scanner.nextLine()
    print("Masukkan tanggal check-out (YYYY-MM-DD): ")
    val checkOut = scanner.nextLine()

    val queryKamar = "SELECT * FROM kamar WHERE nomor = ? AND status = 'Tersedia'"
    val statementKamar = connection.prepareStatement(queryKamar)
    statementKamar.setInt(1, nomorKamar)
    val resultSet = statementKamar.executeQuery()

    if (resultSet.next()) {
        val queryReservasi = "INSERT INTO reservasi (nama, nomor_kamar, check_in, check_out) VALUES (?, ?, ?, ?)"
        val statementReservasi = connection.prepareStatement(queryReservasi)
        statementReservasi.setString(1, nama)
        statementReservasi.setInt(2, nomorKamar)
        statementReservasi.setDate(3, Date.valueOf(checkIn))
        statementReservasi.setDate(4, Date.valueOf(checkOut))
        statementReservasi.executeUpdate()

        val queryUpdateKamar = "UPDATE kamar SET status = 'Terisi' WHERE nomor = ?"
        val statementUpdateKamar = connection.prepareStatement(queryUpdateKamar)
        statementUpdateKamar.setInt(1, nomorKamar)
        statementUpdateKamar.executeUpdate()

        println("Reservasi untuk kamar $nomorKamar atas nama $nama berhasil ditambahkan.")
    } else {
        println("Kamar tidak tersedia.")
    }
}

fun lihatDaftarReservasi(connection: Connection) {
    val query = """
    SELECT DISTINCT r.nomor_kamar, r.nama, k.tipe, k.harga, r.check_in, r.check_out
    FROM reservasi r
    JOIN kamar k ON r.nomor_kamar = k.nomor
""".trimIndent()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)

    println("\n--= Daftar Reservasi =--")
    while (resultSet.next()) {
        val nomorKamar = resultSet.getInt("nomor_kamar")
        val namaTamu = resultSet.getString("nama")
        val tipeKamar = resultSet.getString("tipe")
        val harga = resultSet.getInt("harga")
        val checkIn = resultSet.getDate("check_in")
        val checkOut = resultSet.getDate("check_out")
        println("===================================")
        println("Nama           : ${namaTamu}     ")
        println("Nomor Kamar    : ${nomorKamar}   ")
        println("Tipe Kamar     : ${tipeKamar}    ")
        println("Harga permalam : ${harga}        ")
        println("Check-In       : ${checkIn}      ")
        println("Check-Out      : ${checkOut}     ")
        println("===================================")
    }
}

fun hapusReservasi(scanner: Scanner, connection: Connection) {
    print("Masukkan Nomor Kamar reservasi yang ingin dihapus: ")
    val nomorKamar = scanner.nextLine().toInt()

    val queryReservasi = "SELECT id FROM reservasi WHERE nomor_kamar = ?"
    val statementReservasi = connection.prepareStatement(queryReservasi)
    statementReservasi.setInt(1, nomorKamar)
    val resultSet = statementReservasi.executeQuery()

    if (resultSet.next()) {
        val idReservasi = resultSet.getInt("id")

        val queryDeleteReservasi = "DELETE FROM reservasi WHERE id = ?"
        val statementDeleteReservasi = connection.prepareStatement(queryDeleteReservasi)
        statementDeleteReservasi.setInt(1, idReservasi)
        statementDeleteReservasi.executeUpdate()

        val queryUpdateKamar = "UPDATE kamar SET status = 'Tersedia' WHERE nomor = ?"
        val statementUpdateKamar = connection.prepareStatement(queryUpdateKamar)
        statementUpdateKamar.setInt(1, nomorKamar)
        statementUpdateKamar.executeUpdate()

        println("Reservasi kamar nomor $nomorKamar berhasil dihapus.")
    } else {
        println("Reservasi untuk kamar nomor $nomorKamar tidak ditemukan.")
    }
}


fun cekStatusKamar(connection: Connection) {
    println("\n--= Status Kamar =--")
    val jumlahKamar = 18
    val kamarPerBaris = 6

    val query = "SELECT nomor, status FROM kamar"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)

    val kamarStatus = mutableMapOf<Int, String>()
    while (resultSet.next()) {
        val nomor = resultSet.getInt("nomor")
        val status = resultSet.getString("status")
        kamarStatus[nomor] = status
    }

    var barisKamar = 0
    for (i in 1..jumlahKamar) {
        val status = kamarStatus[i] ?: "Tersedia"
        if (status == "Terisi") {
            print(" X ".padEnd(4))
        } else {
            print(" $i ".padEnd(4))
        }

        barisKamar++
        if (barisKamar == kamarPerBaris) {
            println() // Pindah ke baris berikutnya setelah setiap 6 kamar
            barisKamar = 0
        }
    }
    println()
}


fun rekapPenginapan(connection: Connection) {
    val queryTotalBooked = """
        SELECT COUNT(*) AS totalBooked
        FROM reservasi
    """.trimIndent()

    val queryPendapatanPerHari = """
        SELECT SUM(k.harga) AS totalPendapatan
        FROM reservasi r
        JOIN kamar k ON r.nomor_kamar = k.nomor
    """.trimIndent()

    var totalBooked = 0
    var totalPendapatan = 0

    connection.prepareStatement(queryTotalBooked).use { statement ->
        statement.executeQuery().use { resultSet ->
            if (resultSet.next()) {
                totalBooked = resultSet.getInt("totalBooked")
            }
        }
    }

    connection.prepareStatement(queryPendapatanPerHari).use { statement ->
        statement.executeQuery().use { resultSet ->
            if (resultSet.next()) {
                totalPendapatan = resultSet.getInt("totalPendapatan")
            }
        }
    }

    println("\n--= Rekap Penginapan Risni =--")
    println("Jumlah Kamar Terisi    : $totalBooked")
    println("Total Pemasukan        : Rp$totalPendapatan")
}

