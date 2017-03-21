LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := gemfivelocal
LOCAL_LDLIBS := \
	-llog \
	-lz \
	-lm \

LOCAL_SRC_FILES := \
	D:\android\gemfive\app\src\main\jni\Android.mk \
	D:\android\gemfive\app\src\main\jni\Application.mk \
	D:\android\gemfive\app\src\main\jni\main.c \
	D:\android\gemfive\app\src\main\jni\util.c \

LOCAL_C_INCLUDES += D:\android\gemfive\app\src\main\jni
LOCAL_C_INCLUDES += D:\android\gemfive\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
