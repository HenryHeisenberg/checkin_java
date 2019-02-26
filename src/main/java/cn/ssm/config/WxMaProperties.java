package cn.ssm.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

	private List<Config> configs;

	public static class Config {
		/**
		 * 设置微信小程序的appid
		 */
		private String appid;

		/**
		 * 设置微信小程序的Secret
		 */
		private String secret;

		/**
		 * 设置微信小程序消息服务器配置的token
		 */
		private String token;

		/**
		 * 设置微信小程序消息服务器配置的EncodingAESKey
		 */
		private String aesKey;

		/**
		 * 消息格式，XML或者JSON
		 */
		private String msgDataFormat;

		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getAesKey() {
			return aesKey;
		}

		public void setAesKey(String aesKey) {
			this.aesKey = aesKey;
		}

		public String getMsgDataFormat() {
			return msgDataFormat;
		}

		public void setMsgDataFormat(String msgDataFormat) {
			this.msgDataFormat = msgDataFormat;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aesKey == null) ? 0 : aesKey.hashCode());
			result = prime * result + ((appid == null) ? 0 : appid.hashCode());
			result = prime * result + ((msgDataFormat == null) ? 0 : msgDataFormat.hashCode());
			result = prime * result + ((secret == null) ? 0 : secret.hashCode());
			result = prime * result + ((token == null) ? 0 : token.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Config other = (Config) obj;
			if (aesKey == null) {
				if (other.aesKey != null)
					return false;
			} else if (!aesKey.equals(other.aesKey))
				return false;
			if (appid == null) {
				if (other.appid != null)
					return false;
			} else if (!appid.equals(other.appid))
				return false;
			if (msgDataFormat == null) {
				if (other.msgDataFormat != null)
					return false;
			} else if (!msgDataFormat.equals(other.msgDataFormat))
				return false;
			if (secret == null) {
				if (other.secret != null)
					return false;
			} else if (!secret.equals(other.secret))
				return false;
			if (token == null) {
				if (other.token != null)
					return false;
			} else if (!token.equals(other.token))
				return false;
			return true;
		}

	}

	public List<Config> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configs == null) ? 0 : configs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WxMaProperties other = (WxMaProperties) obj;
		if (configs == null) {
			if (other.configs != null)
				return false;
		} else if (!configs.equals(other.configs))
			return false;
		return true;
	}

}
