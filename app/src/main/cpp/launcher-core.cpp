#include <jni.h>
#include <string>
#include <stdlib.h>
#include <sys/sysinfo.h>

extern "C" JNIEXPORT jlong JNICALL
Java_com_fearlauncher_core_java_NativeLauncher_getTotalRam(
        JNIEnv* env,
        jobject /* this */) {
    struct sysinfo info;
    if (sysinfo(&info) == 0) {
        return (jlong)info.totalram * info.mem_unit;
    }
    return 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_fearlauncher_core_java_NativeLauncher_getRendererName(
        JNIEnv* env,
        jobject /* this */,
        jint rendererId) {
    std::string name = "Unknown";
    switch (rendererId) {
        case 0: name = "Holly Renderer"; break;
        case 1: name = "Zink (Mesa)"; break;
        case 2: name = "LTW Renderer"; break;
        case 3: name = "GL4ES"; break;
    }
    return env->NewStringUTF(name.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_fearlauncher_core_java_NativeLauncher_setEnv(
        JNIEnv* env,
        jobject /* this */,
        jstring key,
        jstring value) {
    const char* nativeKey = env->GetStringUTFChars(key, 0);
    const char* nativeValue = env->GetStringUTFChars(value, 0);
    setenv(nativeKey, nativeValue, 1);
    env->ReleaseStringUTFChars(key, nativeKey);
    env->ReleaseStringUTFChars(value, nativeValue);
}
