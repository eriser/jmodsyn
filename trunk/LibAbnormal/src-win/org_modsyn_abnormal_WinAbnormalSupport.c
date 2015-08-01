#include <stdio.h>
#include <float.h>
#include "org_modsyn_abnormal_WinAbnormalSupport.h"
#include <xmmintrin.h>
#include <assert.h>

#define _MM_DENORMALS_ZERO_MASK   0x0040
#define _MM_DENORMALS_ZERO_ON     0x0040
#define _MM_DENORMALS_ZERO_OFF    0x0000

#define _MM_SET_DENORMALS_ZERO_MODE(mode)                                   \
            _mm_setcsr((_mm_getcsr() & ~_MM_DENORMALS_ZERO_MASK) | (mode))
#define _MM_GET_DENORMALS_ZERO_MODE()                                       \
            (_mm_getcsr() & _MM_DENORMALS_ZERO_MASK)
            
JNIEXPORT void JNICALL Java_org_modsyn_abnormal_WinAbnormalSupport_setDenormals
  (JNIEnv * env, jobject jobj, jboolean enable)
{
	if (enable) {
		_MM_SET_FLUSH_ZERO_MODE(_MM_FLUSH_ZERO_OFF);
        _MM_SET_DENORMALS_ZERO_MODE(_MM_DENORMALS_ZERO_OFF);
	} else {
		_MM_SET_FLUSH_ZERO_MODE(_MM_FLUSH_ZERO_ON);
        _MM_SET_DENORMALS_ZERO_MODE(_MM_DENORMALS_ZERO_ON);
	}
}
