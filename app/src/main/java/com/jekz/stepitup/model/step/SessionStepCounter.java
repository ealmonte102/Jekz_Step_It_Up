package com.jekz.stepitup.model.step;

/**
 * Created by evanalmonte on 12/18/17.
 */

public interface SessionStepCounter {

    void removeSessionListener(SessionListener listener);

    void addSessionListener(SessionListener listener);

    void registerSensor();

    void unregisterSensor();

    void startSession();

    void endSession();

    interface SessionListener {

        /**
         * Sends the session that has transpired to the callback
         *
         * @param session The session created.
         */
        void sessionEnded(Session session);

        void onStepCountIncreased(int stepcount);
    }
}