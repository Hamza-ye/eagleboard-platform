@org.hibernate.annotations.TypeDefs({
    @org.hibernate.annotations.TypeDef(
        name = "Aes128EncryptedString",
        typeClass = com.mass3d.hibernate.encryption.type.EncryptedStringUserType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "encryptor", value = "aes128StringEncryptor")}),

    @org.hibernate.annotations.TypeDef(
        name = "jbObject",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "java.lang.Object")}),

    @org.hibernate.annotations.TypeDef(
        name = "jbPlainString",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryPlainStringType.class),

//@org.hibernate.annotations.TypeDef(
//    name = "jbObjectStyle",
//    typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryType.class,
//    parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.common.ObjectStyle")})

    @org.hibernate.annotations.TypeDef(
        name = "jbTextPattern",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.textpattern.TextPattern")}),

    @org.hibernate.annotations.TypeDef(
        name = "jbRenderType",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.render.DeviceRenderTypeMap")}),

//    @org.hibernate.annotations.TypeDef(
//        name = "jbFilterPeriod",
//        typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryType.class,
//        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.trackedentityfilter.FilterPeriod")}),

    @org.hibernate.annotations.TypeDef(
        name = "jbSet",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonBinaryType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "java.util.HashSet")}),

    @org.hibernate.annotations.TypeDef(
        name = "jbJobParameters",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonJobParametersType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.scheduling.JobParameters")}),

//    @org.hibernate.annotations.TypeDef(
//        name = "jlbEventFilter",
//        typeClass = com.mass3d.hibernate.jsonb.type.JsonListBinaryType.class,
//        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.trackedentityfilter.EventFilter")}),

    @org.hibernate.annotations.TypeDef(
        name = "jlbMentions",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonListBinaryType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.interpretation.Mention")}),

    @org.hibernate.annotations.TypeDef(
        name = "jblTranslations",
        typeClass = com.mass3d.hibernate.jsonb.type.JsonSetBinaryType.class,
        parameters = {@org.hibernate.annotations.Parameter(name = "clazz", value = "com.mass3d.translation.Translation")})

})
package com.mass3d.hibernate.jsonb.type;