package com.juzipi.demo.rsa;

/**
 * rsa加解密用的公钥和私钥
 * @author Administrator
 *
 */
public class RsaKeys {

	//生成秘钥对的方法可以参考这篇帖子
	//https://www.cnblogs.com/yucy/p/8962823.html

//	//服务器公钥
//	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Dw9nwjBmDD/Ca1QnRGy"
//											 + "GjtLbF4CX2EGGS7iqwPToV2UUtTDDemq69P8E+WJ4n5W7Iln3pgK+32y19B4oT5q"
//											 + "iUwXbbEaAXPPZFmT5svPH6XxiQgsiaeZtwQjY61qDga6UH2mYGp0GbrP3i9TjPNt"
//											 + "IeSwUSaH2YZfwNgFWqj+y/0jjl8DUsN2tIFVSNpNTZNQ/VX4Dln0Z5DBPK1mSskd"
//											 + "N6uPUj9Ga/IKnwUIv+wL1VWjLNlUjcEHsUE+YE2FN03VnWDJ/VHs7UdHha4d/nUH"
//											 + "rZrJsKkauqnwJsYbijQU+a0HubwXB7BYMlKovikwNpdMS3+lBzjS5KIu6mRv1GoE"
//											 + "vQIDAQAB";
//
//	//服务器私钥(经过pkcs8格式处理)
//	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDoPD2fCMGYMP8J"
//				 								 + "rVCdEbIaO0tsXgJfYQYZLuKrA9OhXZRS1MMN6arr0/wT5YniflbsiWfemAr7fbLX"
//				 								 + "0HihPmqJTBdtsRoBc89kWZPmy88fpfGJCCyJp5m3BCNjrWoOBrpQfaZganQZus/e"
//				 								 + "L1OM820h5LBRJofZhl/A2AVaqP7L/SOOXwNSw3a0gVVI2k1Nk1D9VfgOWfRnkME8"
//				 								 + "rWZKyR03q49SP0Zr8gqfBQi/7AvVVaMs2VSNwQexQT5gTYU3TdWdYMn9UeztR0eF"
//				 								 + "rh3+dQetmsmwqRq6qfAmxhuKNBT5rQe5vBcHsFgyUqi+KTA2l0xLf6UHONLkoi7q"
//				 								 + "ZG/UagS9AgMBAAECggEBANP72QvIBF8Vqld8+q7FLlu/cDN1BJlniReHsqQEFDOh"
//				 								 + "pfiN+ZZDix9FGz5WMiyqwlGbg1KuWqgBrzRMOTCGNt0oteIM3P4iZlblZZoww9nR"
//				 								 + "sc4xxeXJNQjYIC2mZ75x6bP7Xdl4ko3B9miLrqpksWNUypTopOysOc9f4FNHG326"
//				 								 + "0EMazVaXRCAIapTlcUpcwuRB1HT4N6iKL5Mzk3bzafLxfxbGCgTYiRQNeRyhXOnD"
//				 								 + "eJox64b5QkFjKn2G66B5RFZIQ+V+rOGsQElAMbW95jl0VoxUs6p5aNEe6jTgRzAT"
//				 								 + "kqM2v8As0GWi6yogQlsnR0WBn1ztggXTghQs2iDZ0YkCgYEA/LzC5Q8T15K2bM/N"
//				 								 + "K3ghIDBclB++Lw/xK1eONTXN+pBBqVQETtF3wxy6PiLV6PpJT/JIP27Q9VbtM9UF"
//				 								 + "3lepW6Z03VLqEVZo0fdVVyp8oHqv3I8Vo4JFPBDVxFiezygca/drtGMoce0wLWqu"
//				 								 + "bXlUmQlj+PTbXJMz4VTXuPl1cesCgYEA6zu5k1DsfPolcr3y7K9XpzkwBrT/L7LE"
//				 								 + "EiUGYIvgAkiIta2NDO/BIPdsq6OfkMdycAwkWFiGrJ7/VgU+hffIZwjZesr4HQuC"
//				 								 + "0APsqtUrk2yx+f33ZbrS39gcm/STDkVepeo1dsk2DMp7iCaxttYtMuqz3BNEwfRS"
//				 								 + "kIyKujP5kfcCgYEA1N2vUPm3/pNFLrR+26PcUp4o+2EY785/k7+0uMBOckFZ7GIl"
//				 								 + "FrV6J01k17zDaeyUHs+zZinRuTGzqzo6LSCsNdMnDtos5tleg6nLqRTRzuBGin/A"
//				 								 + "++xWn9aWFT+G0ne4KH9FqbLyd7IMJ9R4gR/1zseH+kFRGNGqmpi48MS61G0CgYBc"
//				 								 + "PBniwotH4cmHOSWkWohTAGBtcNDSghTRTIU4m//kxU4ddoRk+ylN5NZOYqTxXtLn"
//				 								 + "Tkt9/JAp5VoW/41peCOzCsxDkoxAzz+mkrNctKMWdjs+268Cy4Nd09475GU45khb"
//				 								 + "Y/88qV6xGz/evdVW7JniahbGByQhrMwm84R9yF1mNwKBgCIJZOFp9xV2997IY83S"
//				 								 + "habB/YSFbfZyojV+VFBRl4uc6OCpXdtSYzmsaRcMjN6Ikn7Mb9zgRHR8mPmtbVfj"
//				 								 + "B8W6V1H2KOPfn/LAM7Z0qw0MW4jimBhfhn4HY30AQ6GeImb2OqOuh3RBbeuuD+7m"
//				 								 + "LpFZC9zGggf9RK3PfqKeq30q";

	//服务器公钥
	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0KryCEuX5tswP6zXF4md\n" +
			"V3/0coIRwRp6Yv8FlTpO2LdQ/YoxEgXSreMpY5EftCGpLDX7omSKSrptbBodpSVd\n" +
			"RL7gmohu6FZsVqn0Wqc8Rd6U/VGbpzNd9sZclDISzVTAkcndbuFKUahPRDm7VXIv\n" +
			"CbQw8G5GCk5W7H1i3Oy569vqk35ndWnlDDQ8+1+LjqGNUe/wfFN7n29EtfQCDM4E\n" +
			"rbwsDs2hE7iyheQgp6LEW0dJeZCRmaf53/qPIx/MFi/pNFQFHOrYmTPN4Bl46YEd\n" +
			"AJVNwxbXDGq4QwRB1aKEXBKRXJst8Z0WRkbNtQAo7yxbl88z6wcdcrPZagft0oms\n" +
			"DwIDAQAB";

	//服务器私钥(经过pkcs8格式处理)
	private static final String serverPrvKeyPkcs8 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQqvIIS5fm2zA/\n" +
			"rNcXiZ1Xf/RyghHBGnpi/wWVOk7Yt1D9ijESBdKt4yljkR+0IaksNfuiZIpKum1s\n" +
			"Gh2lJV1EvuCaiG7oVmxWqfRapzxF3pT9UZunM132xlyUMhLNVMCRyd1u4UpRqE9E\n" +
			"ObtVci8JtDDwbkYKTlbsfWLc7Lnr2+qTfmd1aeUMNDz7X4uOoY1R7/B8U3ufb0S1\n" +
			"9AIMzgStvCwOzaETuLKF5CCnosRbR0l5kJGZp/nf+o8jH8wWL+k0VAUc6tiZM83g\n" +
			"GXjpgR0AlU3DFtcMarhDBEHVooRcEpFcmy3xnRZGRs21ACjvLFuXzzPrBx1ys9lq\n" +
			"B+3SiawPAgMBAAECggEAGI4FLTS4C1J/uv570SlAeqtz+IWV0UNsY3nfDlBuNtJb\n" +
			"lXpNi/FjeVLh6/WH5YqQfcNceR+bpm+JsM7h7i0XbJ+JjqW/Z1fEJ+6X8pL7ZL1s\n" +
			"iuKvjl1dEU0GV+oMjF0MzBsVE5cvR6ymp0Cj6sok/t9VYvWXCF9vmySmHZk9x6NC\n" +
			"8RCSy+qmp6tj4fp6b4Wq9h6M6nOSi02nuojfde1PYtK2VgtbYy/gaBFIWYP4serz\n" +
			"TUMMG06at8XkR6uhYPJTte0+asZAu7soXI2oe3N8JRHW0KAgv4EJXWkfwBj9WBlK\n" +
			"uYPamkms/tlCNAPgXILNgkVkggKJosW+McC2JDTWAQKBgQDp4J1BMPasFA4l5GhQ\n" +
			"dxk3TJc6tloWygRrCJmxMck+NwjzV9IqjjQRnnq/BJuM828v8SLTIm820roQg7A/\n" +
			"iJ6poFkdSbw+VbU/a/kbrHZlRMkdTeN99ajU48p9wvNWkkQ6eqQeSqV0Vz1DjbTv\n" +
			"7UygW6Yj2sj4/r5bS1ZoYQ1wiQKBgQDkZ9/1hVTdAiwUhX5BIibLAdgUA5Fk3viP\n" +
			"DGdDFhSIUXO21Ks3XdddMB56xaxSTBrY/qhjKup8u3BtLT9ypEhUjINCYV4Fe+30\n" +
			"tDExYlqqK+5RTjLFcxzyS33X8w7djZGyrF5+L9q11VX8b1MLUW/dDus6Tq5ac3LO\n" +
			"Efudtiqh1wKBgFqFm5gd8XvND2TPMSjj0Bi9RlNtPRVQ/8DFxWb2FCRGfOpi9W+P\n" +
			"NswTAQNqLyKxHjn6JTZ/P3iCu42kytTsdizFaXiSj4+48LwpeHOjnve3VfhHq2OX\n" +
			"b89mk3t1t2Wsm0ffLCKlVcDf+1BoSr4KpPv0PSvnhJ4LS3ZehIVF+0OxAoGADSnT\n" +
			"ZXH+txIF+lTLNvPB6pc7ncOq4HZdOtlLJjmii0yub44IJBO7crpwN0EnMkMClRjw\n" +
			"GW8CvpeK3i/qZTAEnyLN+chkw8olp6Gu6Bq0APSxMmxgTrLYqzogkY6Hf8tF51c5\n" +
			"xun5H9ugrgC4d4GFAed3NRANyla2+htdNfSmtlcCgYEAkYS7YcZbZwpC7fMz1228\n" +
			"KO1I0YSuX763p+dp3agmDm5Y7d0l9hm1GzjibtGuVTHHKi+jcVcxsyvc8n31RKxT\n" +
			"GdnBQK69JQHOtFdaLUXCAQ7yCsR5ZD2OcNViCpWqSCfjP95QIGxbFOYIwtIYTWEo\n" +
			"0c2A0pDYZQ3Fb8rOib3l3Qg=";

	public static String getServerPubKey() {
		return serverPubKey;
	}

	public static String getServerPrvKeyPkcs8() {
		return serverPrvKeyPkcs8;
	}
	
}
