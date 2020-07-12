package io.t28.auto.truth.test.data;

import com.google.common.base.Preconditions;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import com.google.common.truth.Truth;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("io.t28.auto.truth.processor.AutoTruthProcessor")
@SuppressWarnings("unchecked")
public class AutoGenericTypesSubject<T> extends Subject {
    private final GenericTypes<T> actual;

    protected AutoGenericTypesSubject(@Nonnull FailureMetadata failureMetadata,
                                      @Nullable GenericTypes<T> actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    @Nonnull
    public static <T> AutoGenericTypesSubject<T> assertThat(@Nullable GenericTypes<T> actual) {
        return Truth.assertAbout(new Subject.Factory<AutoGenericTypesSubject<T>, GenericTypes<T>>() {
            @Override
            public final AutoGenericTypesSubject<T> createSubject(FailureMetadata metadata,
                                                                  GenericTypes<T> _actual) {
                return new AutoGenericTypesSubject<T>(metadata, _actual);
            }
        }).that(actual);
    }

    public void hasValue(T expected) {
        final T actual = Preconditions.checkNotNull(this.actual).value();
        check("value()").that(actual).isEqualTo(expected);
    }
}
