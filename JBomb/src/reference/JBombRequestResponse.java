package reference;

public enum JBombRequestResponse {
	ERROR_FLASH,
	NOTICE_FLASH,
	
	GAME_NOT_FOUND_ERROR,
	GAME_FULL_ERROR,
	
	NEW_GAME_REQUEST,
	NEW_GAME_RESPONSE,
	CREATE_GAME_REQUEST,
	CREATE_GAME_RESPONSE,
	
	GAME_LIST_REQUEST,
	GAME_LIST_RESPONSE,
	JOIN_GAME_REQUEST,
	GAMEPLAY_INFORMATION_RESPONSE,
	PLAYER_ADDED,
	GAME_RUNNABLE,
	START_GAME_REQUEST,
	BOMB_OWNER_RESPONSE,
	SEND_BOMB_REQUEST,
	QUIZ_QUESTION_RESPONSE,
	QUIZ_ANSWER_REQUEST,
	QUIZ_ANSWER_RESPONSE,
	BOMB_REJECTED_RESPONSE,
	GAMEPLAY_STATE_RESPONSE,
	BOMB_DETONATED_RESPONSE,

	CLOSED_CONNECTION,
	
	CLOSE_CONNECTION_RESPONSE,
	CLOSE_CONNECTION_REQUEST,
	
	GAME_SETTINGS_INFORMATION_REQUEST,
	GAME_SETTINGS_INFORMATION_RESPONSE
}