dependencies {
    api(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.json)
    implementation(project(":core-domain"))

    // QueryDSL
    implementation(libs.querydsl.jpa) {
        artifact {
            classifier = "jakarta"
        }
    }
    annotationProcessor(libs.querydsl.apt) {
        artifact {
            classifier = "jakarta"
        }
    }
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.lombok.mapstruct.binding)
    annotationProcessor(libs.mapstruct.processor)

    runtimeOnly(libs.h2)
    runtimeOnly(libs.mysql.connector)
    runtimeOnly(libs.postgresql)
}
