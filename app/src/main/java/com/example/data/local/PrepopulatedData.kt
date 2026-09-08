package com.example.data.local

import com.example.data.model.Exercise
import com.example.data.model.Lesson
import com.example.data.model.VocabularyWord

object PrepopulatedData {

    fun getInitialVocabulary(): List<VocabularyWord> = listOf(
        // SALUDOS
        VocabularyWord(
            otomi = "Haxäi",
            spanish = "Buenos días",
            phoneticIpa = "/ha.ʃæ̃i/",
            phoneticSpanish = "Ja-shäi (vocal nasal)",
            category = "Saludos y Cortesía",
            exampleOtomi = "Haxäi mä me, ¿hanja gi 'müi?",
            exampleSpanish = "Buenos días mamá, ¿cómo estás?",
            culturalNote = "El saludo tradicional matutino alude al amanecer y a la bendición del nuevo sol.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Dejaxäi",
            spanish = "Buenas tardes",
            phoneticIpa = "/de.ha.ʃæ̃i/",
            phoneticSpanish = "De-ja-shäi",
            category = "Saludos y Cortesía",
            exampleOtomi = "Dejaxäi xita, jamädi.",
            exampleSpanish = "Buenas tardes abuelo, gracias.",
            culturalNote = "Usado a partir del mediodía hasta el anochecer en las comunidades del Valle del Mezquital.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Xunde",
            spanish = "Buenas noches",
            phoneticIpa = "/ʃun.de/",
            phoneticSpanish = "Shun-de",
            category = "Saludos y Cortesía",
            exampleOtomi = "Xunde nu'i, gi tsaya xähmä.",
            exampleSpanish = "Buenas noches a ti, descansa bien.",
            culturalNote = "Deseo de descanso y tranquilidad bajo el manto de la noche.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Kogui",
            spanish = "Hola / Qué tal",
            phoneticIpa = "/ko.ɣwi/",
            phoneticSpanish = "Ko-gui",
            category = "Saludos y Cortesía",
            exampleOtomi = "Kogui amigo, ¿hanja gi 'müi?",
            exampleSpanish = "Hola amigo, ¿cómo estás?",
            culturalNote = "Saludo informal y fraterno entre conocidos y familiares.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Jamädi",
            spanish = "Gracias / Dios se lo pague",
            phoneticIpa = "/ha.mæ̃.di/",
            phoneticSpanish = "Ja-mä-di (a nasal)",
            category = "Saludos y Cortesía",
            exampleOtomi = "Jamädi ndunthi por ri mfats'i.",
            exampleSpanish = "Muchas gracias por tu ayuda.",
            culturalNote = "Expresión sagrada de reciprocidad que vincula a quien ayuda con una bendición.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Hñöti",
            spanish = "Por favor",
            phoneticIpa = "/hn̥ø̃.ti/",
            phoneticSpanish = "Jñö-ti",
            category = "Saludos y Cortesía",
            exampleOtomi = "Hñöti, r'aki n'da hme.",
            exampleSpanish = "Por favor, dame una tortilla.",
            culturalNote = "Indica cortesía y respeto hacia los mayores o la anfitriona.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Magö",
            spanish = "Adiós / Ya me voy",
            phoneticIpa = "/ma.gø̃/",
            phoneticSpanish = "Ma-gö",
            category = "Saludos y Cortesía",
            exampleOtomi = "Magö ya, di handi xuk'a.",
            exampleSpanish = "Ya me voy, nos vemos después.",
            culturalNote = "Despedida habitual cuando alguien parte de su casa o reunión.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "¿Hanja gi 'müi?",
            spanish = "¿Cómo estás?",
            phoneticIpa = "/han.ha gi ʔmɯi/",
            phoneticSpanish = "Jan-ja gui 'müi",
            category = "Saludos y Cortesía",
            exampleOtomi = "¿Hanja gi 'müi mä amigo?",
            exampleSpanish = "¿Cómo estás mi amigo?",
            culturalNote = "Pregunta cordial que indaga sobre el bienestar de la persona y su hogar.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Xähmä",
            spanish = "Muy bien / Bien",
            phoneticIpa = "/ʃæ̃h.mæ̃/",
            phoneticSpanish = "Shäj-mä",
            category = "Saludos y Cortesía",
            exampleOtomi = "Dra xähmä, jamädi.",
            exampleSpanish = "Estoy muy bien, gracias.",
            culturalNote = "Respuesta optimista ante el saludo de cortesía.",
            toneType = "Nasal"
        ),

        // NÚMEROS
        VocabularyWord(
            otomi = "N'da",
            spanish = "Uno (1)",
            phoneticIpa = "/nʔda/",
            phoneticSpanish = "N-da con golpe de glotis",
            category = "Números",
            exampleOtomi = "Di ne n'da doji.",
            exampleSpanish = "Quiero un elote.",
            culturalNote = "El saltillo ' indica un corte breve de aire con las cuerdas vocales.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Yoho",
            spanish = "Dos (2)",
            phoneticIpa = "/jo.ho/",
            phoneticSpanish = "Yo-jo",
            category = "Números",
            exampleOtomi = "Hui yoho t'u.",
            exampleSpanish = "Tengo dos hijos.",
            culturalNote = "La dualidad es un concepto fundamental en la cosmovisión otomí.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Hñu",
            spanish = "Tres (3)",
            phoneticIpa = "/hn̥u/",
            phoneticSpanish = "Jñu (aire aspirado por la nariz)",
            category = "Números",
            exampleOtomi = "Hñu ya faní.",
            exampleSpanish = "Tres caballos.",
            culturalNote = "El sonido 'hñ' es una consonante nasal sorda propia del otomí.",
            toneType = "Aspirado"
        ),
        VocabularyWord(
            otomi = "Goho",
            spanish = "Cuatro (4)",
            phoneticIpa = "/go.ho/",
            phoneticSpanish = "Go-jo",
            category = "Números",
            exampleOtomi = "Goho ya m'öti di tsöte.",
            exampleSpanish = "Cuatro pájaros vuelan.",
            culturalNote = "El cuatro representa los cuatro rumbos cardinales de la tierra.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "K'uta",
            spanish = "Cinco (5)",
            phoneticIpa = "/kʔu.ta/",
            phoneticSpanish = "K'u-ta (k glotalizada)",
            category = "Números",
            exampleOtomi = "K'uta ya pa.",
            exampleSpanish = "Cinco días.",
            culturalNote = "Representa los dedos de una mano, base del sistema vigesimal tradicional.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Rät'a",
            spanish = "Seis (6)",
            phoneticIpa = "/ræ̃.tʔa/",
            phoneticSpanish = "Rä-t'a",
            category = "Números",
            exampleOtomi = "Rät'a ya hme.",
            exampleSpanish = "Seis tortillas.",
            culturalNote = "Compuesto formativo que parte de la base 5 + 1 en el sistema histórico.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Yoto",
            spanish = "Siete (7)",
            phoneticIpa = "/jo.to/",
            phoneticSpanish = "Yo-to",
            category = "Números",
            exampleOtomi = "Yoto ya 'mehi.",
            exampleSpanish = "Siete estrellas.",
            culturalNote = "Asociado a las Pléyades y a los ciclos agrícolas de siembra.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Hñäto",
            spanish = "Ocho (8)",
            phoneticIpa = "/hn̥æ̃.to/",
            phoneticSpanish = "Jñä-to",
            category = "Números",
            exampleOtomi = "Hñäto ya wada.",
            exampleSpanish = "Ocho magueyes.",
            culturalNote = "Nótese el fonema hñ seguido de la vocal nasal ä.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Guto",
            spanish = "Nueve (9)",
            phoneticIpa = "/gu.to/",
            phoneticSpanish = "Gu-to",
            category = "Números",
            exampleOtomi = "Guto ya doxi.",
            exampleSpanish = "Nueve piedras.",
            culturalNote = "El último dígito simple antes de la decena.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Rëta",
            spanish = "Diez (10)",
            phoneticIpa = "/rə̃.ta/",
            phoneticSpanish = "Rë-ta (ë cerrada gutural)",
            category = "Números",
            exampleOtomi = "Rëta ya pesos.",
            exampleSpanish = "Diez pesos.",
            culturalNote = "Representa las dos manos completas de la persona.",
            toneType = "Nasal"
        ),

        // FAMILIA
        VocabularyWord(
            otomi = "Mä me",
            spanish = "Mi madre / Mamá",
            phoneticIpa = "/mæ̃ me/",
            phoneticSpanish = "Mä me",
            category = "Familia y Personas",
            exampleOtomi = "Mä me bi tho'ts'i ya hme.",
            exampleSpanish = "Mi madre hizo las tortillas.",
            culturalNote = "La madre es el pilar afectivo y transmisor de la lengua materna en el hogar.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Mä dada",
            spanish = "Mi padre / Papá",
            phoneticIpa = "/mæ̃ da.da/",
            phoneticSpanish = "Mä da-da",
            category = "Familia y Personas",
            exampleOtomi = "Mä dada pefi ha mbonthi.",
            exampleSpanish = "Mi padre trabaja en el campo.",
            culturalNote = "El padre históricamente lidera la labor agrícola en las milpas.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "T'u",
            spanish = "Hijo",
            phoneticIpa = "/tʔu/",
            phoneticSpanish = "T'u (con golpe de glotis)",
            category = "Familia y Personas",
            exampleOtomi = "Nuni ge mä t'u.",
            exampleSpanish = "Aquel es mi hijo.",
            culturalNote = "La ' indica pausa glotal característica de los términos de parentesco.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Tsu",
            spanish = "Hija",
            phoneticIpa = "/tsu/",
            phoneticSpanish = "Tsu",
            category = "Familia y Personas",
            exampleOtomi = "Mä tsu koxqui n'da bordado.",
            exampleSpanish = "Mi hija borda un bordado tenango.",
            culturalNote = "Las hijas aprenden el arte textil del tenangudo y bordado desde temprana edad.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Xita",
            spanish = "Abuelo",
            phoneticIpa = "/ʃi.ta/",
            phoneticSpanish = "Shi-ta",
            category = "Familia y Personas",
            exampleOtomi = "Mä xita xikagi ya historias.",
            exampleSpanish = "Mi abuelo me cuenta historias.",
            culturalNote = "Los abuelos son los guardianes de la memoria histórica y los saberes ancestrales.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Zisu",
            spanish = "Abuela",
            phoneticIpa = "/zi.su/",
            phoneticSpanish = "Zi-su",
            category = "Familia y Personas",
            exampleOtomi = "Mä zisu pädi ndunthi de medicina.",
            exampleSpanish = "Mi abuela sabe mucho de plantas medicinales.",
            culturalNote = "Las curanderas y yerberas tradicionales gozan de gran reverencia en el pueblo hñähñu.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Bähti",
            spanish = "Niño / Joven",
            phoneticIpa = "/bæ̃h.ti/",
            phoneticSpanish = "Bäj-ti",
            category = "Familia y Personas",
            exampleOtomi = "Ya bähti mantsi ha escuela.",
            exampleSpanish = "Los niños van alegres a la escuela.",
            culturalNote = "Nueva generación que hoy revitaliza y aprende con orgullo su lengua originaria.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Bëhñä",
            spanish = "Mujer",
            phoneticIpa = "/bə̃h.ɲæ̃/",
            phoneticSpanish = "Bëj-ñä",
            category = "Familia y Personas",
            exampleOtomi = "Ra bëhñä pefi con empeño.",
            exampleSpanish = "La mujer trabaja con dedicación.",
            culturalNote = "Las mujeres otomíes portan con honor sus fajas y quexquémetl bordados.",
            toneType = "Nasal"
        ),

        // ANIMALES
        VocabularyWord(
            otomi = "Faní",
            spanish = "Caballo",
            phoneticIpa = "/fa.ni˦/",
            phoneticSpanish = "Fa-ní (tono alto)",
            category = "Animales de la Región",
            exampleOtomi = "Mä faní säxte xähmä.",
            exampleSpanish = "Mi caballo corre muy rápido.",
            culturalNote = "Animal noble de transporte y apoyo en los senderos serranos.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Tzätzi",
            spanish = "Perro",
            phoneticIpa = "/tsæ̃.tsi/",
            phoneticSpanish = "Tsä-tsi",
            category = "Animales de la Región",
            exampleOtomi = "Ra tzätzi su ra ngü.",
            exampleSpanish = "El perro cuida la casa.",
            culturalNote = "Compañero leal y guardián de la familia y el ganado.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "M'öti",
            spanish = "Pájaro / Ave",
            phoneticIpa = "/mʔø̃.ti/",
            phoneticSpanish = "M'ö-ti (con vocal ö)",
            category = "Animales de la Región",
            exampleOtomi = "Ra m'öti thuhu xähmä.",
            exampleSpanish = "El pájaro canta muy hermoso.",
            culturalNote = "Las aves silvestres son representadas con vivos colores en los bordados tenangos.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Maxani",
            spanish = "Gato",
            phoneticIpa = "/ma.ʃa.ni/",
            phoneticSpanish = "Ma-sha-ni",
            category = "Animales de la Región",
            exampleOtomi = "Ra maxani a'mi ha hyadi.",
            exampleSpanish = "El gato duerme bajo el sol.",
            culturalNote = "Protector de los graneros contra los roedores.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Doxu",
            spanish = "Pavo / Guajolote",
            phoneticIpa = "/do.ʃu/",
            phoneticSpanish = "Do-shu",
            category = "Animales de la Región",
            exampleOtomi = "Ra doxu ge pa ra ntheti.",
            exampleSpanish = "El guajolote es para la fiesta del casamiento.",
            culturalNote = "Platillo y ave ceremonial imprescindible en bodas y fiestas patronales.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "'Yöi",
            spanish = "Serpiente / Víbora",
            phoneticIpa = "/ʔjø̃i/",
            phoneticSpanish = "'Yöi",
            category = "Animales de la Región",
            exampleOtomi = "N'da 'yöi 'müi ha doxi.",
            exampleSpanish = "Una víbora vive entre las piedras.",
            culturalNote = "Símbolo de sabiduría de la tierra y lluvia en las antiguas narraciones.",
            toneType = "Glotal"
        ),

        // COLORES Y NATURALEZA
        VocabularyWord(
            otomi = "K'angi",
            spanish = "Verde",
            phoneticIpa = "/kʔaŋ.gi/",
            phoneticSpanish = "K'an-gui",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra za ge k'angi.",
            exampleSpanish = "El árbol es verde.",
            culturalNote = "Color de la vida vegetal y de la milpa reverdecida tras el temporal.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "'Boti",
            spanish = "Negro",
            phoneticIpa = "/ʔbo.ti/",
            phoneticSpanish = "'Bo-ti",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra 'boti tzätzi.",
            exampleSpanish = "El perro negro.",
            culturalNote = "La 'B representa una consonante oclusiva con cierre glotal previo.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "'T'axi",
            spanish = "Blanco",
            phoneticIpa = "/ʔtʔa.ʃi/",
            phoneticSpanish = "'T'a-shi",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra 't'axi dängo.",
            exampleSpanish = "El lienzo blanco.",
            culturalNote = "La tela blanca de manta sirve como lienzo para plasmar los hilos coloridos.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Thengi",
            spanish = "Rojo",
            phoneticIpa = "/tʰeŋ.gi/",
            phoneticSpanish = "Ten-gui (t aspirada)",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra thengi 'ñe ge ts'ohui.",
            exampleSpanish = "El chile rojo pica mucho.",
            culturalNote = "Color de la grana cochinilla y la energía solar.",
            toneType = "Aspirado"
        ),
        VocabularyWord(
            otomi = "K'ast'i",
            spanish = "Amarillo",
            phoneticIpa = "/kʔas.tʔi/",
            phoneticSpanish = "K'as-t'i",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra doji ge k'ast'i.",
            exampleSpanish = "El elote es amarillo.",
            culturalNote = "Asociado a los granos de maíz dorado y al cempasúchil.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Dehe",
            spanish = "Agua",
            phoneticIpa = "/de.he/",
            phoneticSpanish = "De-je",
            category = "Colores y Naturaleza",
            exampleOtomi = "Di ne n'da vaso dehe.",
            exampleSpanish = "Quiero un vaso de agua.",
            culturalNote = "Elemento sagrado vital en el árido Valle del Mezquital, reverenciado en rezos.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Hyadi",
            spanish = "Sol / Día",
            phoneticIpa = "/ça.di/",
            phoneticSpanish = "Jya-di",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra hyadi pa xähmä.",
            exampleSpanish = "El sol calienta fuerte hoy.",
            culturalNote = "La deidad dadora de calor y orientadora de la jornada de trabajo.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Zänä",
            spanish = "Luna / Mes",
            phoneticIpa = "/zæ̃.næ̃/",
            phoneticSpanish = "Zä-nä",
            category = "Colores y Naturaleza",
            exampleOtomi = "Ra zänä hyospi ha dehe.",
            exampleSpanish = "La luna se refleja en el agua.",
            culturalNote = "Guía los tiempos de corte de madera, siembra y fermentación del pulque.",
            toneType = "Nasal"
        ),

        // ALIMENTOS Y COCINA
        VocabularyWord(
            otomi = "Hme",
            spanish = "Tortilla de maíz",
            phoneticIpa = "/hme/",
            phoneticSpanish = "Jme",
            category = "Alimentos y Cocina",
            exampleOtomi = "Ra hme pa ra njutsi.",
            exampleSpanish = "La tortilla recién salida del comal.",
            culturalNote = "Alimento primordial hecho de maíz nixtamalizado a mano sobre el comal de barro.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Dengxu",
            spanish = "Frijol",
            phoneticIpa = "/deŋ.ʃu/",
            phoneticSpanish = "Den-gshu",
            category = "Alimentos y Cocina",
            exampleOtomi = "Dengxu ko ya hme.",
            exampleSpanish = "Frijoles con tortillas.",
            culturalNote = "Compañero inseparable del maíz en la milpa y en la dieta ancestral.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "'Ñe",
            spanish = "Chile / Picante",
            phoneticIpa = "/ʔɲe/",
            phoneticSpanish = "'Ñe",
            category = "Alimentos y Cocina",
            exampleOtomi = "Nu'mä 'ñe ge ts'o.",
            exampleSpanish = "Este chile es muy picante.",
            culturalNote = "Indispensable en salsas de molcajete con xoconostle o quelites.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "Tsi",
            spanish = "Comer",
            phoneticIpa = "/tsi/",
            phoneticSpanish = "Tsi",
            category = "Alimentos y Cocina",
            exampleOtomi = "Ma ga tsi ya.",
            exampleSpanish = "Vamos a comer ya.",
            culturalNote = "El acto de compartir la mesa fortalece el lazo comunitario.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "K'ahi",
            spanish = "Pulque",
            phoneticIpa = "/kʔa.hi/",
            phoneticSpanish = "K'a-ji",
            category = "Alimentos y Cocina",
            exampleOtomi = "Ra k'ahi ge zötho.",
            exampleSpanish = "El pulque está dulce y sabroso.",
            culturalNote = "Bebida ancestral de los dioses elaborada a partir del aguamiel de maguey.",
            toneType = "Glotal"
        ),

        // CUERPO HUMANO
        VocabularyWord(
            otomi = "Da",
            spanish = "Ojo",
            phoneticIpa = "/da/",
            phoneticSpanish = "Da",
            category = "Cuerpo Humano",
            exampleOtomi = "Mä da handi ra hyadi.",
            exampleSpanish = "Mis ojos miran al sol.",
            culturalNote = "Ventana del alma y de la atención en el aprendizaje visual.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Ne",
            spanish = "Boca",
            phoneticIpa = "/ne/",
            phoneticSpanish = "Ne",
            category = "Cuerpo Humano",
            exampleOtomi = "Ne pa ma ga ñä.",
            exampleSpanish = "Boca para que hablemos.",
            culturalNote = "Vehículo del hñähñu: la palabra hablada con respeto.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Gu",
            spanish = "Oreja / Oído",
            phoneticIpa = "/gu/",
            phoneticSpanish = "Gu",
            category = "Cuerpo Humano",
            exampleOtomi = "Ode ko ri gu.",
            exampleSpanish = "Escucha con tus oídos.",
            culturalNote = "La tradición otomí se transmite principalmente a través de la escucha atenta.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "K'ä",
            spanish = "Mano",
            phoneticIpa = "/kʔæ̃/",
            phoneticSpanish = "K'ä (con nasal y glotal)",
            category = "Cuerpo Humano",
            exampleOtomi = "Ri k'ä pefi xähmä.",
            exampleSpanish = "Tus manos trabajan muy bien.",
            culturalNote = "Con las manos se siembra, se teje el ixtle de maguey y se borda con amor.",
            toneType = "Glotal"
        ),
        VocabularyWord(
            otomi = "'Muhu",
            spanish = "Corazón / Sentimiento",
            phoneticIpa = "/ʔmu.hu/",
            phoneticSpanish = "'Mu-ju",
            category = "Cuerpo Humano",
            exampleOtomi = "Di mä'i ko mä 'muhu.",
            exampleSpanish = "Te quiero con todo mi corazón.",
            culturalNote = "En otomí el corazón es también el centro del pensamiento y las emociones.",
            toneType = "Glotal"
        ),

        // FRASES COTIDIANAS
        VocabularyWord(
            otomi = "¿Hanja gi thuhu?",
            spanish = "¿Cómo te llamas?",
            phoneticIpa = "/han.ha gi tʰu.hu/",
            phoneticSpanish = "Jan-ja gui tu-ju",
            category = "Frases Cotidianas",
            exampleOtomi = "Kogui, ¿hanja gi thuhu nu'i?",
            exampleSpanish = "Hola, ¿cómo te llamas tú?",
            culturalNote = "Pregunta inicial para conocer a una persona y entablar amistad.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Mä thuhu...",
            spanish = "Mi nombre es...",
            phoneticIpa = "/mæ̃ tʰu.hu/",
            phoneticSpanish = "Mä tu-ju...",
            category = "Frases Cotidianas",
            exampleOtomi = "Mä thuhu Mateo.",
            exampleSpanish = "Mi nombre es Mateo.",
            culturalNote = "Respuesta común de presentación personal.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "¿Habu gi ma?",
            spanish = "¿A dónde vas?",
            phoneticIpa = "/ha.bu gi ma/",
            phoneticSpanish = "Ja-bu gui ma",
            category = "Frases Cotidianas",
            exampleOtomi = "¿Habu gi ma mä amigo?",
            exampleSpanish = "¿A dónde vas mi amigo?",
            culturalNote = "Saludo callejero frecuente al cruzarse en el camino del pueblo.",
            toneType = "Normal"
        ),
        VocabularyWord(
            otomi = "Di nämä",
            spanish = "Te quiero / Te aprecio",
            phoneticIpa = "/di næ̃.mæ̃/",
            phoneticSpanish = "Di nä-mä",
            category = "Frases Cotidianas",
            exampleOtomi = "Mä me, di nämä ndunthi.",
            exampleSpanish = "Mamá, te quiero muchísimo.",
            culturalNote = "Expresión tierna de amor filial y afecto fraternal.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Hã",
            spanish = "Sí (Afirmación)",
            phoneticIpa = "/hã/",
            phoneticSpanish = "Jã (a nasal)",
            category = "Frases Cotidianas",
            exampleOtomi = "Hã, ma ga ma.",
            exampleSpanish = "Sí, vamos a ir.",
            culturalNote = "Respuesta afirmativa con aspiración suave y nasalidad.",
            toneType = "Nasal"
        ),
        VocabularyWord(
            otomi = "Hina",
            spanish = "No (Negación)",
            phoneticIpa = "/hi.na/",
            phoneticSpanish = "Ji-na",
            category = "Frases Cotidianas",
            exampleOtomi = "Hina, hin di pädi.",
            exampleSpanish = "No, no lo sé.",
            culturalNote = "Palabra clara de negación o declinación cortés.",
            toneType = "Normal"
        )
    )

    fun getLessons(): List<Lesson> = listOf(
        Lesson(
            id = "lesson_1",
            title = "Primeros Saludos en Hñähñu",
            unit = "Unidad 1: Empezando a Hablar",
            category = "Saludos y Cortesía",
            level = "Principiante",
            description = "Aprende las frases esenciales para saludar, agradecer y presentarte con respeto.",
            xpReward = 50,
            exercises = listOf(
                Exercise.FlashcardIntro(
                    otomiWord = "Haxäi",
                    spanishTranslation = "Buenos días",
                    phoneticGuide = "Ja-shäi (vocal nasal ä)",
                    tip = "El saludo tradicional al amanecer. La letra ä se pronuncia por la nariz con una sonrisa amplia.",
                    exampleSentence = "Haxäi mä me (Buenos días mamá)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Jamädi",
                    spanishTranslation = "Gracias / Dios se lo pague",
                    phoneticGuide = "Ja-mä-di",
                    tip = "Una de las palabras más bellas del otomí, expresa agradecimiento sagrado y bendición.",
                    exampleSentence = "Jamädi ndunthi (Muchas gracias)"
                ),
                Exercise.MultipleChoice(
                    question = "¿Qué significa 'Haxäi' en español?",
                    promptAudioWord = "Haxäi",
                    options = listOf("Buenas noches", "Buenos días", "Por favor", "Adiós"),
                    correctIndex = 1,
                    explanation = "¡Correcto! 'Haxäi' es el saludo otomí matutino para decir 'Buenos días'."
                ),
                Exercise.SentenceBuilder(
                    promptSpanish = "Traduce: 'Buenos días mamá'",
                    scrambledOtomiTokens = listOf("mä", "Haxäi", "me", "dada"),
                    correctOtomiSentence = "Haxäi mä me",
                    explanation = "'Haxäi' (Buenos días) + 'mä me' (mi mamá)."
                ),
                Exercise.MultipleChoice(
                    question = "¿Cómo se dice 'Gracias' en otomí?",
                    promptAudioWord = "Jamädi",
                    options = listOf("Hina", "Magö", "Jamädi", "Kogui"),
                    correctIndex = 2,
                    explanation = "'Jamädi' significa gracias o que Dios se lo pague."
                ),
                Exercise.PronunciationExercise(
                    otomiWord = "Haxäi",
                    spanishTranslation = "Buenos días",
                    phoneticHint = "Ja-shäi",
                    culturalContext = "Practica pronunciar la 'x' como 'sh' suave y la 'ä' con resonancia nasal."
                )
            )
        ),
        Lesson(
            id = "lesson_2",
            title = "Contar del 1 al 5",
            unit = "Unidad 1: Empezando a Hablar",
            category = "Números",
            level = "Principiante",
            description = "Domina los primeros cinco números y el singular sonido del saltillo (') glotal.",
            xpReward = 60,
            exercises = listOf(
                Exercise.FlashcardIntro(
                    otomiWord = "N'da",
                    spanishTranslation = "Uno (1)",
                    phoneticGuide = "N-da (corte glotal)",
                    tip = "El apóstrofo ' indica un corte seco de voz en la garganta (saltillo).",
                    exampleSentence = "N'da dehe (Un poco de agua)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Yoho",
                    spanishTranslation = "Dos (2)",
                    phoneticGuide = "Yo-jo",
                    tip = "Fácil de pronunciar: Yo-jo con 'j' suave mexicana.",
                    exampleSentence = "Yoho ya faní (Dos caballos)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Hñu",
                    spanishTranslation = "Tres (3)",
                    phoneticGuide = "Jñu (aire por la nariz)",
                    tip = "El dígrafo 'hñ' se produce expulsando aire sordo por la nariz mientras dices 'ñ'.",
                    exampleSentence = "Hñu ya m'öti (Tres pájaros)"
                ),
                Exercise.WordMatch(
                    prompt = "Une cada número con su significado en español",
                    pairs = listOf(
                        "N'da" to "Uno (1)",
                        "Yoho" to "Dos (2)",
                        "Hñu" to "Tres (3)",
                        "Goho" to "Cuatro (4)"
                    )
                ),
                Exercise.MultipleChoice(
                    question = "¿Qué número representa la palabra 'Hñu'?",
                    promptAudioWord = "Hñu",
                    options = listOf("Uno", "Cinco", "Tres", "Dos"),
                    correctIndex = 2,
                    explanation = "Hñu = Tres (3)."
                ),
                Exercise.PronunciationExercise(
                    otomiWord = "N'da",
                    spanishTranslation = "Uno",
                    phoneticHint = "N'da con pausa glotal",
                    culturalContext = "Detén el aire en la laringe antes de soltar la 'da'."
                )
            )
        ),
        Lesson(
            id = "lesson_3",
            title = "Mi Familia y el Hogar",
            unit = "Unidad 2: Vida y Comunidad",
            category = "Familia y Personas",
            level = "Básico",
            description = "Nombres para mamá, papá, abuelos y niños en el entorno familiar otomí.",
            xpReward = 70,
            exercises = listOf(
                Exercise.FlashcardIntro(
                    otomiWord = "Mä dada",
                    spanishTranslation = "Mi padre / Papá",
                    phoneticGuide = "Mä da-da",
                    tip = "'Mä' es el posesivo 'mi', y 'dada' significa padre.",
                    exampleSentence = "Mä dada pefi (Mi padre trabaja)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Xita",
                    spanishTranslation = "Abuelo",
                    phoneticGuide = "Shi-ta",
                    tip = "Recuerda que la 'x' en la ortografía hñähñu suena como 'sh'.",
                    exampleSentence = "Mä xita (Mi abuelo)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Zisu",
                    spanishTranslation = "Abuela",
                    phoneticGuide = "Zi-su",
                    tip = "La abuela, figura de gran cariño y respeto en la comunidad.",
                    exampleSentence = "Mä zisu (Mi abuela)"
                ),
                Exercise.MultipleChoice(
                    question = "¿Qué significa 'Mä me'?",
                    promptAudioWord = "Mä me",
                    options = listOf("Mi hija", "Mi madre", "Mi tía", "Mi abuela"),
                    correctIndex = 1,
                    explanation = "¡Exacto! 'Mä me' significa 'Mi madre' o 'Mi mamá'."
                ),
                Exercise.SentenceBuilder(
                    promptSpanish = "Traduce: 'Mi abuelo y mi abuela'",
                    scrambledOtomiTokens = listOf("Mä", "xita", "ne", "mä", "zisu"),
                    correctOtomiSentence = "Mä xita ne mä zisu",
                    explanation = "'Ne' funciona como conjunción 'y' (Mä xita ne mä zisu)."
                ),
                Exercise.PronunciationExercise(
                    otomiWord = "Xita",
                    spanishTranslation = "Abuelo",
                    phoneticHint = "Shi-ta",
                    culturalContext = "Sonido suave 'sh' al principio, similar a 'show'."
                )
            )
        ),
        Lesson(
            id = "lesson_4",
            title = "Animales del Valle",
            unit = "Unidad 2: Vida y Comunidad",
            category = "Animales de la Región",
            level = "Básico",
            description = "Conoce cómo se nombran el caballo, el perro, las aves y el gato en hñähñu.",
            xpReward = 75,
            exercises = listOf(
                Exercise.FlashcardIntro(
                    otomiWord = "Faní",
                    spanishTranslation = "Caballo",
                    phoneticGuide = "Fa-ní",
                    tip = "Tiene un tono agudo al final (marcado con acento).",
                    exampleSentence = "Ra faní säxte (El caballo corre)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Tzätzi",
                    spanishTranslation = "Perro",
                    phoneticGuide = "Tsä-tsi",
                    tip = "La 'tz' suena como 'ts' o la 'z' italiana en pizza.",
                    exampleSentence = "Ra tzätzi su ra ngü (El perro cuida la casa)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "M'öti",
                    spanishTranslation = "Pájaro / Ave",
                    phoneticGuide = "M'ö-ti (ö nasal redondeada)",
                    tip = "La 'ö' se pronuncia con labios redondeados como en francés 'eu' o alemán 'ö'.",
                    exampleSentence = "Ra m'öti thuhu (El pájaro canta)"
                ),
                Exercise.MultipleChoice(
                    question = "¿Cómo se dice 'Pájaro' o 'Ave' en otomí?",
                    promptAudioWord = "M'öti",
                    options = listOf("Faní", "Tzätzi", "M'öti", "Maxani"),
                    correctIndex = 2,
                    explanation = "'M'öti' es la palabra para pájaro o ave."
                ),
                Exercise.WordMatch(
                    prompt = "Relaciona los animales con su nombre en otomí",
                    pairs = listOf(
                        "Faní" to "Caballo",
                        "Tzätzi" to "Perro",
                        "Maxani" to "Gato",
                        "Doxu" to "Pavo / Guajolote"
                    )
                ),
                Exercise.PronunciationExercise(
                    otomiWord = "M'öti",
                    spanishTranslation = "Pájaro",
                    phoneticHint = "M'ö-ti",
                    culturalContext = "Redondea los labios para la 'ö' con un corte breve tras la 'm'."
                )
            )
        ),
        Lesson(
            id = "lesson_5",
            title = "Colores y Naturaleza Viva",
            unit = "Unidad 3: El Paisaje y los Sentidos",
            category = "Colores y Naturaleza",
            level = "Intermedio",
            description = "Aprende los colores tradicionales de los bordados y elementos como agua y sol.",
            xpReward = 80,
            exercises = listOf(
                Exercise.FlashcardIntro(
                    otomiWord = "K'angi",
                    spanishTranslation = "Verde",
                    phoneticGuide = "K'an-gui",
                    tip = "El color de los nopales, magueyes y hojas de los árboles.",
                    exampleSentence = "Ra za ge k'angi (El árbol es verde)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Thengi",
                    spanishTranslation = "Rojo",
                    phoneticGuide = "Ten-gui (t con soplo de aire)",
                    tip = "Color del fuego, de la grana cochinilla y de los bordados florales.",
                    exampleSentence = "Ra thengi dängo (El lienzo rojo)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "Dehe",
                    spanishTranslation = "Agua",
                    phoneticGuide = "De-je",
                    tip = "Palabra sagrada y vital para la siembra y la vida humana.",
                    exampleSentence = "Di ne dehe (Quiero agua)"
                ),
                Exercise.MultipleChoice(
                    question = "¿Qué color significa 'Thengi'?",
                    promptAudioWord = "Thengi",
                    options = listOf("Blanco", "Negro", "Rojo", "Verde"),
                    correctIndex = 2,
                    explanation = "'Thengi' significa Rojo en otomí."
                ),
                Exercise.SentenceBuilder(
                    promptSpanish = "Traduce: 'Quiero agua por favor'",
                    scrambledOtomiTokens = listOf("Di", "ne", "dehe", "hñöti"),
                    correctOtomiSentence = "Di ne dehe hñöti",
                    explanation = "'Di ne' (Quiero) + 'dehe' (agua) + 'hñöti' (por favor)."
                ),
                Exercise.PronunciationExercise(
                    otomiWord = "Dehe",
                    spanishTranslation = "Agua",
                    phoneticHint = "De-je",
                    culturalContext = "La 'h' suena como la 'j' en español mexicano suave."
                )
            )
        ),
        Lesson(
            id = "lesson_6",
            title = "En el Mercado y la Cocina",
            unit = "Unidad 3: El Paisaje y los Sentidos",
            category = "Alimentos y Cocina",
            level = "Intermedio",
            description = "Palabras para la tortilla recién hecha, el frijol, el chile y el elote.",
            xpReward = 85,
            exercises = listOf(
                Exercise.FlashcardIntro(
                    otomiWord = "Hme",
                    spanishTranslation = "Tortilla de maíz",
                    phoneticGuide = "Jme",
                    tip = "El alimento central que nunca falta en la mesa otomí.",
                    exampleSentence = "Tsi n'da hme (Come una tortilla)"
                ),
                Exercise.FlashcardIntro(
                    otomiWord = "'Ñe",
                    spanishTranslation = "Chile / Picante",
                    phoneticGuide = "'Ñe (glotal antes de la ñ)",
                    tip = "El ingrediente picante que sazona toda buena salsa tradicional.",
                    exampleSentence = "Ra 'ñe ts'o (El chile pica)"
                ),
                Exercise.MultipleChoice(
                    question = "¿Cómo se dice 'Tortilla de maíz' en hñähñu?",
                    promptAudioWord = "Hme",
                    options = listOf("Hme", "Dehe", "Dengxu", "K'ahi"),
                    correctIndex = 0,
                    explanation = "'Hme' es la tortilla de maíz."
                ),
                Exercise.WordMatch(
                    prompt = "Une los alimentos tradicionales con su significado",
                    pairs = listOf(
                        "Hme" to "Tortilla",
                        "Dengxu" to "Frijol",
                        "'Ñe" to "Chile",
                        "K'ahi" to "Pulque"
                    )
                ),
                Exercise.PronunciationExercise(
                    otomiWord = "Hme",
                    spanishTranslation = "Tortilla de maíz",
                    phoneticHint = "Jme",
                    culturalContext = "Aspira suavemente la h antes de sonar la m."
                )
            )
        )
    )
}
