export interface responseMsg {
	predicted_age?: number;
	calling_detection?: number;
	error?: string;
	user_text?: string;
	predicted_action?: string;
}

export interface requestMsg {
	image?: string;
	audio?: ArrayBuffer;
}
