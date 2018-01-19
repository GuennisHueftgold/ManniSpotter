package com.semeshky.kvgspotter.api;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public final class Release {

    private final String mUrl;
    private final long mId;
    private final String mTagName;
    private final String mName;
    private final boolean mDraft;
    private final boolean mPreRelease;
    private final String mHtmlUrl;

    private Release(Builder builder) {
        this.mUrl = builder.getUrl();
        this.mId = builder.getId();
        this.mTagName = builder.getTagName();
        this.mName = builder.getName();
        this.mDraft = builder.isDraft();
        this.mPreRelease = builder.isPreRelease();
        this.mHtmlUrl = builder.getHtmlUrl();
    }

    public String getUrl() {
        return mUrl;
    }

    public long getId() {
        return mId;
    }

    public String getTagName() {
        return mTagName;
    }

    public String getName() {
        return mName;
    }

    public boolean isDraft() {
        return mDraft;
    }

    public boolean isPreRelease() {
        return mPreRelease;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    @Override
    public String toString() {
        return "Release{" +
                "mUrl='" + mUrl + '\'' +
                ", mId=" + mId +
                ", mTagName='" + mTagName + '\'' +
                ", mName='" + mName + '\'' +
                ", mDraft=" + mDraft +
                ", mPreRelease=" + mPreRelease +
                ", mHtmlUrl='" + mHtmlUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Release release = (Release) o;

        if (mId != release.mId) return false;
        if (mDraft != release.mDraft) return false;
        if (mPreRelease != release.mPreRelease) return false;
        if (mUrl != null ? !mUrl.equals(release.mUrl) : release.mUrl != null) return false;
        if (mTagName != null ? !mTagName.equals(release.mTagName) : release.mTagName != null)
            return false;
        if (mName != null ? !mName.equals(release.mName) : release.mName != null) return false;
        return mHtmlUrl != null ? mHtmlUrl.equals(release.mHtmlUrl) : release.mHtmlUrl == null;
    }

    @Override
    public int hashCode() {
        int result = mUrl != null ? mUrl.hashCode() : 0;
        result = 31 * result + (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mTagName != null ? mTagName.hashCode() : 0);
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mDraft ? 1 : 0);
        result = 31 * result + (mPreRelease ? 1 : 0);
        result = 31 * result + (mHtmlUrl != null ? mHtmlUrl.hashCode() : 0);
        return result;
    }

    public final static class Builder {
        private String mUrl;
        private long mId;
        private String mTagName;
        private String mName;
        private boolean mDraft;
        private boolean mPreRelease;
        private String mHtmlUrl;

        public String getUrl() {
            return mUrl;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public long getId() {
            return mId;
        }

        public Builder setId(long id) {
            mId = id;
            return this;
        }

        public String getTagName() {
            return mTagName;
        }

        public Builder setTagName(String tagName) {
            mTagName = tagName;
            return this;
        }

        public String getName() {
            return mName;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public boolean isDraft() {
            return mDraft;
        }

        public Builder setDraft(boolean draft) {
            mDraft = draft;
            return this;
        }

        public boolean isPreRelease() {
            return mPreRelease;
        }

        public Builder setPreRelease(boolean preRelease) {
            mPreRelease = preRelease;
            return this;
        }

        public String getHtmlUrl() {
            return mHtmlUrl;
        }

        public Builder setHtmlUrl(String htmlUrl) {
            mHtmlUrl = htmlUrl;
            return this;
        }

        public Release build() {
            return new Release(this);
        }
    }

    static final class TypeAdapter extends com.google.gson.TypeAdapter<Release> {
        final static String NAME_URL = "url",
                NAME_HTML_URL = "html_url",
                NAME_ID = "id",
                NAME_TAG_NAME = "tag_name",
                NAME_DRAFT = "draft",
                NAME_PRERELEASE = "prerelease",
                NAME_NAME = "name";

        @Override
        public void write(JsonWriter out, Release value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name(NAME_URL).value(value.getUrl());
            out.name(NAME_HTML_URL).value(value.getHtmlUrl());
            out.name(NAME_ID).value(value.getId());
            out.name(NAME_TAG_NAME).value(value.getTagName());
            out.name(NAME_DRAFT).value(value.isDraft());
            out.name(NAME_PRERELEASE).value(value.isPreRelease());
            out.name(NAME_NAME).value(value.getName());
            out.endObject();
        }

        @Override
        public Release read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            Builder builder = new Builder();
            in.beginObject();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                switch (name) {
                    case NAME_URL:
                        builder.setUrl(in.nextString());
                        break;
                    case NAME_HTML_URL:
                        builder.setHtmlUrl(in.nextString());
                        break;
                    case NAME_ID:
                        builder.setId(in.nextLong());
                        break;
                    case NAME_TAG_NAME:
                        builder.setTagName(in.nextString());
                        break;
                    case NAME_NAME:
                        builder.setName(in.nextString());
                        break;
                    case NAME_DRAFT:
                        builder.setDraft(in.nextBoolean());
                        break;
                    case NAME_PRERELEASE:
                        builder.setPreRelease(in.nextBoolean());
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}
