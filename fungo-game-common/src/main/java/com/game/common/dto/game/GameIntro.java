package com.game.common.dto.game;

public class GameIntro {

	private String gameId;
	private String packageName;
	
	public String getGameId() {
		return gameId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
//	public static void main(String[] args) throws Exception {
//		String gameintro = "{\"gameId\":\"f774ec1454bb4771b13bfcd8031667ff\",\"packageName\":\"hehehe\"}";
//		String encrypt = AesUtil.getInstance().encrypt(gameintro);
//		System.out.println(encrypt);
//		String decrypt = AesUtil.getInstance().decrypt("U+XgvXJDBOMkthg660avFqDIV42X3t+TQ+Od1DknfXAqwr8FDCMvwhcfAmOEm3yZ6OD+ptZXOib9KiWQiBIL0arIPRk20ej8cHNB7ZOT3hc=");
//		System.out.println(decrypt);
//		GameIntro parseObject = JSON.parseObject(decrypt,GameIntro.class);
//		System.out.println(parseObject);
//	}
	@Override
	public String toString() {
		return "GameIntro [gameId=" + gameId + ", packageName=" + packageName + "]";
	}
	
	
}
