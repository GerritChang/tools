package top.gerritchang.tools.encryption

import com.sun.org.apache.xml.internal.security.utils.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Encryption {

    private fun encode(value: String): String {
        try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
            val digest:ByteArray = instance.digest(value.toByteArray())//对字符串加密，返回字节数组
            var sb = StringBuffer()
            for (b in digest) {
                var i :Int = b.toInt() and 0xff//获取低八位有效值
                var hexString = Integer.toHexString(i)//将整数转化为16进制
                if (hexString.length < 2) {
                    hexString = "0" + hexString//如果是一位的话，补0
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun encrypt(input: String, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance("AES")
        //初始化cipher
        //通过秘钥工厂生产秘钥
        val keySpec = SecretKeySpec(password.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        //加密、解密
        val encrypt = cipher.doFinal(input.toByteArray())
        return Base64.encode(encrypt)
    }

    //解密
    fun decrypt(input: String, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance("AES")
        //初始化cipher
        //通过秘钥工厂生产秘钥
        val keySpec = SecretKeySpec(password.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        //加密、解密
        val encrypt = cipher.doFinal(Base64.decode(input))
        return String(encrypt)
    }

}
