package com.qc.mycomic.test;

import com.qc.mycomic.util.DecryptUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TestUtil {

    public static void main(String[] args) {
        TestUtil util = new TestUtil();
        String jsCode = "" +
                "\tvar _0x1f5dx1 = \"=\",\n" +
                "\t\t_0x1f5dx2 = \"ABCDESFGHIJKLMNOPQRTUVWXYZabcdefghiujklmn9opqrstvwxyz041235678+/\",\n" +
                "\t\t_0x1f5dx3 = \"1.0\";" +
                "\tfunction _0x1f5dx4(_0x1f5dx5, _0x1f5dx6) {\n" +
                "\t\tvar _0x1f5dx7 = _0x1f5dx2[\"indexOf\"](_0x1f5dx5[\"charAt\"](_0x1f5dx6));\n" +
                "\t\tif (_0x1f5dx7 === -1) {\n" +
                "\t\t\tthrow \"Cannot decode base64\"\n" +
                "\t\t};\n" +
                "\t\treturn _0x1f5dx7\n" +
                "\t}" +
                "\tfunction _0x1f5dx8(_0x1f5dx5) {\n" +
                "\t\tvar _0x1f5dx9 = 0,\n" +
                "\t\t\t_0x1f5dx6, _0x1f5dxa, _0x1f5dxb = _0x1f5dx5[\"length\"],\n" +
                "\t\t\t_0x1f5dxc = [];\n" +
                "\t\t_0x1f5dx5 = String(_0x1f5dx5);\n" +
                "\t\tif (_0x1f5dxb === 0) {\n" +
                "\t\t\treturn _0x1f5dx5\n" +
                "\t\t};\n" +
                "\t\tif (_0x1f5dxb % 4 !== 0) {\n" +
                "\t\t\tthrow \"Cannot decode base64\"\n" +
                "\t\t};\n" +
                "\t\tif (_0x1f5dx5[\"charAt\"](_0x1f5dxb - 1) === _0x1f5dx1) {\n" +
                "\t\t\t_0x1f5dx9 = 1;\n" +
                "\t\t\tif (_0x1f5dx5[\"charAt\"](_0x1f5dxb - 2) === _0x1f5dx1) {\n" +
                "\t\t\t\t_0x1f5dx9 = 2\n" +
                "\t\t\t};\n" +
                "\t\t\t_0x1f5dxb -= 4\n" +
                "\t\t};\n" +
                "\t\tfor (_0x1f5dx6 = 0; _0x1f5dx6 < _0x1f5dxb; _0x1f5dx6 += 4) {\n" +
                "\t\t\t_0x1f5dxa = (_0x1f5dx4(_0x1f5dx5, _0x1f5dx6) << 18) | (_0x1f5dx4(_0x1f5dx5, _0x1f5dx6 + 1) << 12) | (_0x1f5dx4(\n" +
                "\t\t\t\t_0x1f5dx5, _0x1f5dx6 + 2) << 6) | _0x1f5dx4(_0x1f5dx5, _0x1f5dx6 + 3);\n" +
                "\t\t\t_0x1f5dxc[\"push\"](String[\"fromCharCode\"](_0x1f5dxa >> 16, (_0x1f5dxa >> 8) & 255, _0x1f5dxa & 255))\n" +
                "\t\t};\n" +
                "\t\tswitch (_0x1f5dx9) {\n" +
                "\t\t\tcase 1:\n" +
                "\t\t\t\t_0x1f5dxa = (_0x1f5dx4(_0x1f5dx5, _0x1f5dx6) << 18) | (_0x1f5dx4(_0x1f5dx5, _0x1f5dx6 + 1) << 12) | (_0x1f5dx4(\n" +
                "\t\t\t\t\t_0x1f5dx5, _0x1f5dx6 + 2) << 6);\n" +
                "\t\t\t\t_0x1f5dxc[\"push\"](String[\"fromCharCode\"](_0x1f5dxa >> 16, (_0x1f5dxa >> 8) & 255));\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t\tcase 2:\n" +
                "\t\t\t\t_0x1f5dxa = (_0x1f5dx4(_0x1f5dx5, _0x1f5dx6) << 18) | (_0x1f5dx4(_0x1f5dx5, _0x1f5dx6 + 1) << 12);\n" +
                "\t\t\t\t_0x1f5dxc[\"push\"](String[\"fromCharCode\"](_0x1f5dxa >> 16));\n" +
                "\t\t\t\tbreak\n" +
                "\t\t};\n" +
                "\t\treturn _0x1f5dxc[\"join\"](\"\")\n" +
                "\t}";
        String exeStr = "dlSxHGdzZuE8HuExLyP0Muc2NWM4ZmIzZ4giNvngHCAgHCAgHCAgHCAgHCAgHCAgHCAgHCB4YXHgd4hhdSQnZUZ0Y4q8Hj3oPTMULXBNTUQCYU8VRjZNQTkzWTIzT0dYWkBiV0UyVjdILUr5UjIkV1gvVlrud4VGaEQTLlwUTuIqRFI5a1kjV1AyTTMqWSZ5RkIMRE2zWuSCPz05bSMhLz9MV0dvTFIFcEMMQ1QtUTMjPkQsTm9RRFwWUVhNV09Vc1hKLE9sYyBSLVdreSSiRS9tWkVJeSIHQlkkQXBxZSU3MkkUPmZuQSn4ZF9CVlIXdEkuQjUyVuMjTkVFZSkjbWh4VUQjelSoVuVja1QYYuA3d4QpVmQVekEyVF3JVFSXMXkUeTkTVTIFVFSoUlhUL4d4WlwvLE8ScE0OWEkxUX9Md0dXeFwUa4PyVl0jTU3HcGBkVSIETuSJUSZEQmVVVEZ3Y4q0Tj0rWmMjVXQRTuBFTkkqZGIMLEZZTWzzLjw5ZSkXQkY4WVhvU0QsVl8iQ1BCWl3aL0S5Ul0VLSZLYVdFWU8YRTQRQj3QTuIuclVHQjduV4hpZVc0RU2ybGZTLlQZZWwaUWSpVmVhekZEYVhjT0S5ZSdTL4wKYUQQMFQXVkSaVyUwTuMqMk9YZEVWVkIoTVdNL4VrRkITVSZRWTIMckIqQmBjLF82ZWrueWHvNXdja0ZzVX9QL0QsRkQhVyV3VGj3U0UxQkIMQjZHVUZvL0IGYyVkVlQtVl3JdVkWbSSaQykMV4wBd0MYTj9XQWQFU4rJdkLwTjdZLVZPZEdWRWQYbDVVL4wTYuMnLkSWVjMkVWQKYuSRcVkscSdNQUnxTDMWRSSrTXIiV09xU0c3akPxdShZbWhtZVZnMSZpQkMuLGBWZWwCMlSEUuVXVykVUzhFLkI5bGMjV1gyVuSCTVUvTk9OQ4LyVjZZeknwTuZkWS9pYTMNeSMVdG9VLzUzTWq0TFIqcFhiL4hMT0hCbSHxQX9XbmBIVVQqRE2xZDVXbEZGWTSQd4VYRkQaek9KTDMqbFQWUjrZVVI1VUQneVYxMTBXVk92UkdMclVXaSMMV1B2YjQRUSMWVj9RQ4r3VjQIekSVTkZiLSZaVlrJLVSrZGkQaz3HWkQRL0QrbFwhbUZ3TVhJTV9oWXdiLGByV0ZFL0jvdF3MbTkUTzL3YWSVTm9WVySVZFwCakSrNUrkVWhFTW3vTVMETl8ubS9rYUQaU0LxWXdNLVHxYVVNTU3SaG9LejIrV402MVSrc19VLuVrUVVId0nxTjMVU1QQVUQmNRH6CiAgHCAgHCAgHCAgHCAgHCAgHCAgHCAgHGZhciB1dFYxOV8fY4QkY1I3cGPnd1QlLRvgP1I3cGQtRkLsZW3uKjIhc4U4MC3vYXIyZRh1aFSzVFhkQmVuaxjsdF8TdGI9blcnP1I3cGQtRkLsZW3uKkVzZug9JTqJHCAgHCAgHCAgHCAgHCAgHCAgHCAgHCAgZXZhbCh1dFYxJTq=";
        String res = DecryptUtil.exeJsFunction(jsCode, "_0x1f5dx8", exeStr);
        System.out.println("res = " + res);
        String cData = "a29ydGl3RktIZSs2WGhPS3M4ZW1yNjFqaG54dXNZeE5OaXQ1UTVWTUlacWRWL0dxYjJZcEpBUE1PMDN3Nmx6c0U2ZHZkSUVEeW1wVXd6aUJwUmV0d2pvK1NkaDdxbldtZWxQcjhnQ3l2c2FiRFgwVjY1MFJzYzgvbTAzY3I2WTRMZmg4R3lmU1F3ckpnL2cxTHEwMHplWXZPMW1vZjZkYmlaaUlPTVVOamR3ZUx5ei9PVStuOFZzOGtEbmtBOUhKa014ckJwdytJZXcwd1BaUzJKMVJaSmlnam1RQmhFcVBWdzBnMCtqY2hNdmxwU3JINjNtK1laZlpsWTRmeExGd3lScE5GenZSZ2NMSGMxNWFCQ2l2eUlzQ0JVUHMxWFB3SnlTUFM4QmthQ3JvcUNYSWN1VGw2UkFCci8ySlU2alNsMjNUclRzeHdET3RPY2Y1SjVEYlhEdGZWa09XaU5PTlNpWXc3dXlIM0tIMFkyd0lVNUFnb0hEbXhyeXVYZUNoVklpOERmazQyNXpwZGI4N2plV0ovdmRCMnVVdk1OUkNjUFpYK1RPVmxwbjJoZ2xOTk1jeXoxM0FZREpqbGd2N2NWeFY4SzE0dDRYbDJ0TWxpemxwcUtmeXZTZXg4WkhTOWl0SitwMXRLU3ZmVm5adWhoY2YwWU5Ea25ObWNNQTdQRHRNWk9oT0QrK3hwN0tjMFBmMjFqZEtkSTBrNVRhZjJXZ1hJeTZNQnNhMDgydUlPL0pYVEtsU3NQSDVDQlR1c0R2VCtzUWJGOHl0WXA0NlozR1NEdWhoQ0o5ZGtXTUlNTzNHSGIxNXorVT0=";
        String dData = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(cData), "fw12558899ertyui");
        System.out.println("dData = " + dData);
        String encCode1 = "Z256UnBySG5tNllCR3BrTUF1NDBQUT09";
        String resStr1 = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(encCode1), "fw12558899ertyui");
        System.out.println("resStr1 = " + resStr1);
        String encCode2 = "TUhmRHYvK2dsa3Y4bWhWMGFGWVlwTnk1VGpDbmxxd3YvRGVxQ2gwTHo4WU5JaDF4cEI3Sk1XUHFqY2k5SUtUZg==";
        String resStr = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(encCode2), "fw125gjdi9ertyui");
        System.out.println("resStr = " + resStr);
    }

}

