package com.jayway.jsonpath;

import com.jayway.jsonpath.internal.DefaultsImpl;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class Configuration {
    private final Collection<EvaluationListener> evaluationListeners;
    private final JsonProvider jsonProvider;
    private final MappingProvider mappingProvider;
    private final Set<Option> options;
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static Defaults DEFAULTS = null;

    /* loaded from: classes.dex */
    public interface Defaults {
        JsonProvider jsonProvider();

        MappingProvider mappingProvider();

        Set<Option> options();
    }

    static /* synthetic */ Defaults access$000() {
        return getEffectiveDefaults();
    }

    public static synchronized void setDefaults(Defaults defaults) {
        synchronized (Configuration.class) {
            DEFAULTS = defaults;
        }
    }

    private static Defaults getEffectiveDefaults() {
        return DEFAULTS == null ? DefaultsImpl.INSTANCE : DEFAULTS;
    }

    private Configuration(JsonProvider jsonProvider, MappingProvider mappingProvider, EnumSet<Option> options, Collection<EvaluationListener> evaluationListeners) {
        Utils.notNull(jsonProvider, "jsonProvider can not be null", new Object[0]);
        Utils.notNull(mappingProvider, "mappingProvider can not be null", new Object[0]);
        Utils.notNull(options, "setOptions can not be null", new Object[0]);
        Utils.notNull(evaluationListeners, "evaluationListeners can not be null", new Object[0]);
        this.jsonProvider = jsonProvider;
        this.mappingProvider = mappingProvider;
        this.options = Collections.unmodifiableSet(options);
        this.evaluationListeners = Collections.unmodifiableCollection(evaluationListeners);
    }

    public Configuration addEvaluationListeners(EvaluationListener... evaluationListener) {
        return builder().jsonProvider(this.jsonProvider).mappingProvider(this.mappingProvider).options(this.options).evaluationListener(evaluationListener).build();
    }

    public Configuration setEvaluationListeners(EvaluationListener... evaluationListener) {
        return builder().jsonProvider(this.jsonProvider).mappingProvider(this.mappingProvider).options(this.options).evaluationListener(evaluationListener).build();
    }

    public Collection<EvaluationListener> getEvaluationListeners() {
        return this.evaluationListeners;
    }

    public Configuration jsonProvider(JsonProvider newJsonProvider) {
        return builder().jsonProvider(newJsonProvider).mappingProvider(this.mappingProvider).options(this.options).evaluationListener(this.evaluationListeners).build();
    }

    public JsonProvider jsonProvider() {
        return this.jsonProvider;
    }

    public Configuration mappingProvider(MappingProvider newMappingProvider) {
        return builder().jsonProvider(this.jsonProvider).mappingProvider(newMappingProvider).options(this.options).evaluationListener(this.evaluationListeners).build();
    }

    public MappingProvider mappingProvider() {
        return this.mappingProvider;
    }

    public Configuration addOptions(Option... options) {
        EnumSet<Option> opts = EnumSet.noneOf(Option.class);
        opts.addAll(this.options);
        opts.addAll(Arrays.asList(options));
        return builder().jsonProvider(this.jsonProvider).mappingProvider(this.mappingProvider).options(opts).evaluationListener(this.evaluationListeners).build();
    }

    public Configuration setOptions(Option... options) {
        return builder().jsonProvider(this.jsonProvider).mappingProvider(this.mappingProvider).options(options).evaluationListener(this.evaluationListeners).build();
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public boolean containsOption(Option option) {
        return this.options.contains(option);
    }

    public static Configuration defaultConfiguration() {
        Defaults defaults = getEffectiveDefaults();
        return builder().jsonProvider(defaults.jsonProvider()).options(defaults.options()).build();
    }

    public static ConfigurationBuilder builder() {
        return new ConfigurationBuilder();
    }

    /* loaded from: classes.dex */
    public static class ConfigurationBuilder {
        private JsonProvider jsonProvider;
        private MappingProvider mappingProvider;
        private EnumSet<Option> options = EnumSet.noneOf(Option.class);
        private Collection<EvaluationListener> evaluationListener = new ArrayList();

        public ConfigurationBuilder jsonProvider(JsonProvider provider) {
            this.jsonProvider = provider;
            return this;
        }

        public ConfigurationBuilder mappingProvider(MappingProvider provider) {
            this.mappingProvider = provider;
            return this;
        }

        public ConfigurationBuilder options(Option... flags) {
            if (flags.length > 0) {
                this.options.addAll(Arrays.asList(flags));
            }
            return this;
        }

        public ConfigurationBuilder options(Set<Option> options) {
            this.options.addAll(options);
            return this;
        }

        public ConfigurationBuilder evaluationListener(EvaluationListener... listener) {
            this.evaluationListener = Arrays.asList(listener);
            return this;
        }

        public ConfigurationBuilder evaluationListener(Collection<EvaluationListener> listeners) {
            if (listeners == null) {
                listeners = Collections.emptyList();
            }
            this.evaluationListener = listeners;
            return this;
        }

        public Configuration build() {
            Defaults defaults = Configuration.access$000();
            if (this.jsonProvider == null) {
                this.jsonProvider = defaults.jsonProvider();
            }
            if (this.mappingProvider == null) {
                this.mappingProvider = defaults.mappingProvider();
            }
            return new Configuration(this.jsonProvider, this.mappingProvider, this.options, this.evaluationListener);
        }
    }
}
