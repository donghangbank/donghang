export interface responseMsg {
	predicted_age?: number;
	calling_detection?: number;
	error?: string;
}

export interface requestMsg {
	image?: string;
	audio?: Blob;
}
