dependencies {
    api(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.json)

    runtimeOnly(libs.h2)
    runtimeOnly(libs.mysql.connector)
    runtimeOnly(libs.postgresql)
}