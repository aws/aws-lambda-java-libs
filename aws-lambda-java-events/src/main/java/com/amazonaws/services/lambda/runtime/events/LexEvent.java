/*
 * Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.Map;

/**
 * represents a Lex event
 */
public class LexEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 8660021082133163891L;

    private String messageVersion;

    private String invocationSource;

    private String userId;

    private Map<String, String> sessionAttributes;

    private String outputDialogMode;

    private CurrentIntent currentIntent;

    private Bot bot;

    /**
     * Represents a Lex bot
     */
    public class Bot implements Serializable, Cloneable {

        private static final long serialVersionUID = -5764739951985883358L;

        private String name;

        private String alias;

        private String version;

        /**
         * default constructor
         */
        public Bot() {}

        /**
         * @return name of bot
         */
        public String getName() {
            return this.name;
        }

        /**
         * @param name name of bot
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @param name name of bot
         * @return Bot object
         */
        public Bot withName(String name) {
            setName(name);
            return this;
        }

        /**
         * @return alias of bot
         */
        public String getAlias() {
            return this.alias;
        }

        /**
         * @param alias alias of bot
         */
        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Bot withAlias(String alias) {
            setAlias(alias);
            return this;
        }

        /**
         * @return version of bot
         */
        public String getVersion() {
            return this.version;
        }

        /**
         * @param version set version of bot
         */
        public void setVersion(String version) {
            this.version = version;
        }

        /**
         * @param version version of bot
         * @return Bot
         */
        public Bot withVersion(String version) {
            setVersion(version);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getName() != null)
                sb.append("name: ").append(getName()).append(",");
            if (getAlias() != null)
                sb.append("alias: ").append(getAlias()).append(",");
            if (getVersion() != null)
                sb.append("version: ").append(getVersion());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Bot == false)
                return false;
            Bot other = (Bot) obj;
            if (other.getName() == null ^ this.getName() == null)
                return false;
            if (other.getName() != null && other.getName().equals(this.getName()) == false)
                return false;
            if (other.getAlias() == null ^ this.getAlias() == null)
                return false;
            if (other.getAlias() != null && other.getAlias().equals(this.getAlias()) == false)
                return false;
            if (other.getVersion() == null ^ this.getVersion() == null)
                return false;
            if (other.getVersion() != null && other.getVersion().equals(this.getVersion()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getName() == null) ? 0 : getName().hashCode());
            hashCode = prime * hashCode + ((getAlias() == null) ? 0 : getAlias().hashCode());
            hashCode = prime * hashCode + ((getVersion() == null) ? 0 : getVersion().hashCode());

            return hashCode;
        }

        @Override
        public Bot clone() {
            try {
                return (Bot) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * models CurrentIntent of Lex event
     */
    public class CurrentIntent implements Serializable, Cloneable {

        private static final long serialVersionUID = 7405357938118538229L;

        private String name;

        private Map<String, String> slots;

        private String confirmationStatus;

        /**
         * default constructor
         */
        public CurrentIntent() {}

        /**
         * @return name of bot
         */
        public String getName() {
            return this.name;
        }

        /**
         * @param name name of bot
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @param name name of intent
         * @return Current Intent
         */
        public CurrentIntent withName(String name) {
            setName(name);
            return this;
        }

        /**
         * @return map of slots
         */
        public Map<String, String> getSlots() {
            return this.slots;
        }

        /**
         * @param slots map of slots
         */
        public void setSlots(Map<String, String> slots) {
            this.slots = slots;
        }

        /**
         * @param slots slots in CurrentIntent
         * @return CurrentIntent
         */
        public CurrentIntent withSlots(Map<String, String> slots) {
            setSlots(slots);
            return this;
        }

        /**
         * @return confirmation status
         */
        public String getConfirmationStatus() {
            return this.confirmationStatus;
        }

        /**
         * @param confirmationStatus confirmation status
         */
        public void setConfirmationStatus(String confirmationStatus) {
            this.confirmationStatus = confirmationStatus;
        }

        /**
         * @param confirmationStatus confirmation status
         * @return CurrentIntent
         */
        public CurrentIntent withConfirmationStatus(String confirmationStatus) {
            setConfirmationStatus(confirmationStatus);
            return this;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getName() != null)
                sb.append("name: ").append(getName()).append(",");
            if (getSlots() != null)
                sb.append("slots: ").append(getSlots().toString()).append(",");
            if (getConfirmationStatus() != null)
                sb.append("confirmationStatus: ").append(getConfirmationStatus());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof CurrentIntent == false)
                return false;
            CurrentIntent other = (CurrentIntent) obj;
            if (other.getName() == null ^ this.getName() == null)
                return false;
            if (other.getName() != null && other.getName().equals(this.getName()) == false)
                return false;
            if (other.getSlots() == null ^ this.getSlots() == null)
                return false;
            if (other.getSlots() != null && other.getSlots().equals(this.getSlots()) == false)
                return false;
            if (other.getConfirmationStatus() == null ^ this.getConfirmationStatus() == null)
                return false;
            if (other.getConfirmationStatus() != null && other.getConfirmationStatus().equals(this.getConfirmationStatus()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getName() == null) ? 0 : getName().hashCode());
            hashCode = prime * hashCode + ((getSlots() == null) ? 0 : getSlots().hashCode());
            hashCode = prime * hashCode + ((getConfirmationStatus() == null) ? 0 : getConfirmationStatus().hashCode());

            return hashCode;
        }

        @Override
        public CurrentIntent clone() {
            try {
                return (CurrentIntent) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * default constructor
     */
    public LexEvent() {}

    /**
     * @return message version
     */
    public String getMessageVersion() {
        return this.messageVersion;
    }

    /**
     * @param messageVersion message version
     */
    public void setMessageVersion(String messageVersion) {
        this.messageVersion = messageVersion;
    }

    /**
     * @param messageVersion message version
     * @return LexEvent
     */
    public LexEvent withMessageVersion(String messageVersion) {
        setMessageVersion(messageVersion);
        return this;
    }

    /**
     * @return source of invocation
     */
    public String getInvocationSource() {
        return this.invocationSource;
    }

    /**
     * @param invocationSource source of invocation
     */
    public void setInvocationSource(String invocationSource) {
        this.invocationSource = invocationSource;
    }

    /**
     * @param invocationSource invokation source
     * @return LexEvent
     */
    public LexEvent withInvocationSource(String invocationSource) {
        setInvocationSource(invocationSource);
        return this;
    }

    /**
     * @return user id
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * @param userId user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param userId user id
     * @return LexEvent
     */
    public LexEvent withUserId(String userId) {
        setUserId(userId);
        return this;
    }

    /**
     * @return session attributes
     */
    public Map<String, String> getSessionAttributes() {
        return this.sessionAttributes;
    }

    /**
     * @param sessionAttributes session attributes
     */
    public void setSessionAttributes(Map<String, String> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    /**
     * @param sessionAttributes session attributes
     * @return LexEvent
     */
    public LexEvent withSessionAttributes(Map<String, String> sessionAttributes) {
        setSessionAttributes(sessionAttributes);
        return this;
    }

    /**
     * @return output dialog mode
     */
    public String getOutputDialogMode() {
        return this.outputDialogMode;
    }

    /**
     * @param outputDialogMode output dialog mode
     */
    public void setOutputDialogMode(String outputDialogMode) {
        this.outputDialogMode = outputDialogMode;
    }

    /**
     * @param outputDialogMode output dialog mode
     * @return LexEvent
     */
    public LexEvent withOutputDialogMode(String outputDialogMode) {
        setOutputDialogMode(outputDialogMode);
        return this;
    }

    /**
     * @return current intent
     */
    public CurrentIntent getCurrentIntent() {
        return  this.currentIntent;
    }

    /**
     * @param currentIntent current intent
     */
    public void setCurrentIntent(CurrentIntent currentIntent) {
        this.currentIntent = currentIntent;
    }

    /**
     * @param currentIntent current intent
     * @return LexEvent
     */
    public LexEvent withCurrentIntent(CurrentIntent currentIntent) {
        setCurrentIntent(currentIntent);
        return this;
    }

    /**
     * @return bot
     */
    public Bot getBot() {
        return this.bot;
    }

    /**
     * @param bot Bot object of Lex message
     */
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    /**
     * @param bot Bot object of message
     * @return LexEvent
     */
    public LexEvent withBot(Bot bot) {
        setBot(bot);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getMessageVersion() != null)
            sb.append("messageVersion: ").append(getMessageVersion()).append(",");
        if (getInvocationSource() != null)
            sb.append("invocationSource: ").append(getInvocationSource()).append(",");
        if (getUserId() != null)
            sb.append("userId: ").append(getUserId()).append(",");
        if (getSessionAttributes() != null)
            sb.append("sessionAttributes: ").append(getSessionAttributes().toString()).append(",");
        if (getOutputDialogMode() != null)
            sb.append("outputDialogMode: ").append(getOutputDialogMode()).append(",");
        if (getCurrentIntent() != null)
            sb.append("currentIntent: ").append(getCurrentIntent().toString()).append(",");
        if (getBot() != null)
            sb.append("bot: ").append(getBot().toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof LexEvent == false)
            return false;
        LexEvent other = (LexEvent) obj;
        if (other.getMessageVersion() == null ^ this.getMessageVersion() == null)
            return false;
        if (other.getMessageVersion() != null && other.getMessageVersion().equals(this.getMessageVersion()) == false)
            return false;
        if (other.getInvocationSource() == null ^ this.getInvocationSource() == null)
            return false;
        if (other.getInvocationSource() != null && other.getInvocationSource().equals(this.getInvocationSource()) == false)
            return false;
        if (other.getUserId() == null ^ this.getUserId() == null)
            return false;
        if (other.getUserId() != null && other.getUserId().equals(this.getUserId()) == false)
            return false;
        if (other.getSessionAttributes() == null ^ this.getSessionAttributes() == null)
            return false;
        if (other.getSessionAttributes() != null && other.getSessionAttributes().equals(this.getSessionAttributes()) == false)
            return false;
        if (other.getOutputDialogMode() == null ^ this.getOutputDialogMode() == null)
            return false;
        if (other.getOutputDialogMode() != null && other.getOutputDialogMode().equals(this.getOutputDialogMode()) == false)
            return false;
        if (other.getCurrentIntent() == null ^ this.getCurrentIntent() == null)
            return false;
        if (other.getCurrentIntent() != null && other.getCurrentIntent().equals(this.getCurrentIntent()) == false)
            return false;
        if (other.getBot() == null ^ this.getBot() == null)
            return false;
        if (other.getBot() != null && other.getBot().equals(this.getBot()) == false)
            return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getMessageVersion() == null) ? 0 : getMessageVersion().hashCode());
        hashCode = prime * hashCode + ((getInvocationSource() == null) ? 0 : getInvocationSource().hashCode());
        hashCode = prime * hashCode + ((getUserId() == null) ? 0 : getUserId().hashCode());
        hashCode = prime * hashCode + ((getSessionAttributes() == null) ? 0 : getSessionAttributes().hashCode());
        hashCode = prime * hashCode + ((getOutputDialogMode() == null) ? 0 : getOutputDialogMode().hashCode());
        hashCode = prime * hashCode + ((getCurrentIntent() == null) ? 0 : getCurrentIntent().hashCode());
        hashCode = prime * hashCode + ((getBot() == null) ? 0 : getBot().hashCode());

        return hashCode;
    }

    @Override
    public LexEvent clone() {
        try {
            return (LexEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
