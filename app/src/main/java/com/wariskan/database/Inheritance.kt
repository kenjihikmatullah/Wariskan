package com.kenji.waris.database

import android.content.Context
import android.util.Log
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kenji.waris.model.*
import com.kenji.waris.model.Gender.*
import com.kenji.waris.model.Position.*
import com.kenji.waris.util.thatEligible
import com.kenji.waris.util.thatEligibleIsExist
import com.kenji.waris.util.thatEligibleIsMany
import com.wariskan.R.string.*
import com.wariskan.model.person.*
import com.wariskan.model.position.*

@Entity(tableName = "inheritance_table")
class Inheritance {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    @Embedded(prefix = "preference_")
    var preference = Preference()

    private val kalalah: Boolean
        get() {
            return !(dad.eligible || children.sons.thatEligibleIsExist() || grandchildren.sonsOfSons.thatEligibleIsExist())
        }

    @Embedded(prefix = "inheritee_")
    var deceased = Deceased()

    @Embedded(prefix = "dad_")
    var dad = Heir().apply { gender = MALE }

    @Embedded(prefix = "mom_")
    var mom = Heir().apply { gender = FEMALE }

    var husband = mutableListOf<Heir>()

    var wives = mutableListOf<Heir>()

    @Embedded(prefix = "children_")
    var children = Children()

    @Embedded(prefix = "siblings_")
    var siblings = Siblings()

    @Embedded(prefix = "grandpas_")
    var grandpas = Grandpas()

    @Embedded(prefix = "grandmas_")
    var grandmas = Grandmas()

    @Embedded(prefix = "grandchildren_")
    var grandchildren = Grandchildren()

    @Embedded(prefix = "uncles_")
    var uncles = Uncles()

    @Embedded(prefix = "maleCousins_")
    var maleCousins = MaleCousins()

    @Embedded(prefix = "nephews_")
    var nephews = Nephews()

    @Ignore
    fun calculate(context: Context) {

        fun updateEligible() {

            var eligibleBefore = true

            /**
             * Children of sons
             * is disentitled by sons
             */
            grandchildren.childrenOfSons.forEach {
                it.eligibleTwo = !(children.sons.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_children_of_sons)
            }

            /**
             * Dad of dad
             * is disentitled by dad
             */
            grandpas.dadOfDad.forEach {
                it.eligibleTwo = !(dad.eligible)
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_dad_of_dad)
            }

            /*
             * Mom of dad
             */
            grandmas.momOfDad.forEach {
                it.eligibleTwo = !(dad.eligible || mom.eligible)
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_mom_of_dad)
            }

            /*
             * Mom of mom
             */
            grandmas.momOfMom.forEach {
                it.eligibleTwo = !(mom.eligible)
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_mom_of_mom)
            }

            /**
             * Full brothers
             * is disentitled by
             * - dad
             * - sons
             */
            siblings.fullBrothers.forEach {
                it.eligibleTwo = !(dad.eligible || children.sons.thatEligibleIsExist())
                eligibleBefore = it.eligibleTwo
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_full_brothers)
            }

            /**
             * Paternal brothers
             * is disentitled by
             * - dad
             * - sons
             * - ...
             * - full brothers
             */
            siblings.paternalBrothers.forEach {
                it.eligibleTwo = !(eligibleBefore || siblings.fullBrothers.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_paternal_brothers)
            }

            /**
             * Maternal brothers
             * is disentitled by
             * - dad
             * - ...
             * - dad of dad
             * - children
             */
            siblings.maternalBrothers.forEach {
                it.eligibleTwo =
                    !(dad.eligible || grandpas.dadOfDad.thatEligibleIsExist() || children.children.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_maternal_brothers)
            }

//            siblings.paternalSisters.forEach {
//                it.eligibleTwo =
//                    !(children.sons.thatEligibleIsExist() || grandchildren.sonsOfSons.thatEligibleIsExist()
//                            || dad.eligible || siblings.fullBrothers.thatEligibleIsExist())
//            }

            /**
             * Sons of full brothers
             * is disentitled by
             * - dad
             * - dad of dad
             * - sons
             * - sons of sons
             * - full brothers
             * - paternal brothers
             */
            nephews.sonsOfFullBrothers.forEach {
                it.eligibleTwo =
                    !(eligibleBefore || grandpas.dadOfDad.thatEligibleIsExist() || grandchildren.sonsOfSons.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist())
                eligibleBefore = it.eligibleTwo
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_sons_of_full_brothers)
            }

            /**
             * Sons of paternal brothers
             * is disentitled by
             * - dad
             * - dad of dad
             * - sons
             * - sons of sons
             * - full brothers
             * - paternal brothers
             * - ...
             * - sons of full brothers
             */
            nephews.sonsOfPaternalBrothers.forEach {
                it.eligibleTwo =
                    !(eligibleBefore || nephews.sonsOfFullBrothers.thatEligibleIsExist())
                eligibleBefore = it.eligibleTwo
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_sons_of_paternal_brothers)
            }

            /**
             * Full brothers of dad
             * is disentitled by
             * - dad
             * - dad of dad
             * - sons
             * - sons of sons
             * - full brothers
             * - paternal brothers
             * - sons of full brothers
             * - ...
             * - sons of paternal brothers
             */
            uncles.fullBrothersOfDad.forEach {
                it.eligibleTwo =
                    !(eligibleBefore || nephews.sonsOfPaternalBrothers.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_full_brothers_of_dad)
            }

            /**
             * Paternal brothers of dad
             * is disentitled by
             * - dad
             * - dad of dad
             * - sons
             * - sons of sons
             * - full brothers
             * - paternal brothers
             * - sons of full brothers
             * - sons of paternal brothers
             * - ...
             * - full brothers of dad
             */
            uncles.paternalBrothersOfDad.forEach {
                it.eligibleTwo = !(eligibleBefore || siblings.fullBrothers.thatEligibleIsExist())
                eligibleBefore = it.eligibleTwo
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_paternal_brothers_of_dad)
            }

            /**
             * Sons of paternal brothers of dad
             * is disentitled by
             * - dad
             * - dad of dad
             * - sons
             * - sons of sons
             * - full brothers
             * - paternal brothers
             * - sons of full brothers
             * - sons of paternal brothers
             * - full brothers of dad
             * - ...
             * - paternalBrothersOfDad
             */
            maleCousins.sonsOfPaternalBrothersOfDad.forEach {
                it.eligibleTwo =
                    !(eligibleBefore || uncles.paternalBrothersOfDad.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_sons_of_paternal_brothers_of_dad)
            }
        }

        fun sharePrimary() {

            fun shareIt(
                legacy: Legacy,
                list: List<Heir>,
                multiplier: Int,
                divider: Int
            ) {

                /*
                 * Record the amount of shared primary inheritance
                 */
                if (list.isNotEmpty()) {
                    legacy.primaryShared += legacy.shareable / divider * multiplier
                    Log.i("HEHEHE", "From: ${list[0].name}")
                    Log.i("HEHEHE", "Just shared: ${legacy.shareable / divider * multiplier}")
                }

                /*
                 * Share the inheritance evenly
                 */
                list.forEach {
                    it.`in`.primary += legacy.shareable / divider / list.size * multiplier
                }
            }

            /*
             * 1/2
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Husband
                 * Suami mendapatkan 1/2 jika pewaris tidak mempunyai anak atau cucu.
                 * QS An Nisa : 12
                 * KHI : 179
                 */
                husband.thatEligible().let {
                    if (children.children.isNotEmpty()) return@let
                    shareIt(out, it, 1, 2)
                    husband.forEach { it.`in`.one = context.getString(husband_1_2) }
                }

                /*
                 * Daughters
                 * Anak perempuan mendapatkan 1/2 jika sendiri dan pewaris tidak mempunyai anak atau cucu laki.
                 * QS An Nisa : 11
                 * KHI : 176
                 */
                children.daughters.thatEligible().let { list ->
                    if (children.sons.isNotEmpty() || list.size > 1) return@let
                    shareIt(out, list, 1, 2)
                    list.forEach { it.`in`.one = context.getString(daughter_1_2) }
                }

                /*
                 * Daughters of sons
                 */
                grandchildren.daughtersOfSons.thatEligible().let { list ->
                    if (children.daughters.thatEligibleIsExist() || children.sons.thatEligibleIsExist() || list.size > 1) return@let
                    shareIt(out, list, 1, 2)
                    list.forEach { it.`in`.one = context.getString(daughter_of_son_1_2) }
                }

                /*
                 * Full sisters
                 * Saudara perempuan seayah seibu mendapatkan 1/2 jika sendiri dan kalalah.
                 */
                siblings.fullSisters.thatEligible().let { list ->
                    if (!kalalah || list.size > 1) return@let
                    shareIt(out, list, 1, 2)
                    list.forEach { it.`in`.one = context.getString(full_sister_1_2) }
                }

                /*
                 * Paternal sisters
                 * get 1/2 if alone and no paternal brothers
                 */
                siblings.paternalSisters.thatEligible().let { list ->
                    if (!kalalah || siblings.paternalBrothers.thatEligibleIsExist() || list.size > 1) return@let
                    shareIt(out, list, 1, 2)
                    list.forEach { it.`in`.one = context.getString(paternal_sister_1_2) }
                }
            }

            /*
             * 1/4
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Husband
                 * Suami mendapatkan 1/4 jika pewaris mempunyai anak atau cucu.
                 * An Nisa (4) : 12
                 * KHI : 180
                 */
                husband.thatEligible().let {
                    if (children.children.isEmpty()) return@let
                    shareIt(out, it, 1, 4)
                    husband.forEach { it.`in`.one = context.getString(husband_1_4) }
                }

                /*
                 * Wives
                 * Istri mendapatkan 1/4 jika pewaris tidak mempunyai anak atau cucu.
                 * Dibagi rata jika bersama.
                 * QS An Nisa : 12
                 */
                wives.thatEligible().let { list ->
                    if (children.children.isNotEmpty()) return@let
                    shareIt(out, list, 1, 4)
                    wives.forEach { it.`in`.one = context.getString(wives_1_4) }
                }
            }

            /*
             * 1/8
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Wives
                 * Istri mendapatkan 1/8 jika pewaris mempunyai anak.
                 * Dibagi rata jika bersama.
                 * QS An Nisa : 12
                 */
                wives.thatEligible().let { list ->
                    if (children.children.isEmpty()) return@let
                    shareIt(out, list, 1, 8)
                    wives.forEach { it.`in`.one = context.getString(wives_1_8) }
                }
            }

            /*
             * 2/3
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Daughters
                 * Anak perempuan mendapatkan 2/3 jika bersama dan tidak ada anak laki.
                 * Dibagi rata.
                 * An Nisa 4:11
                 * KHI : 176
                 */
                children.daughters.thatEligible().let { list ->
                    if (children.sons.isNotEmpty() || list.size <= 1) return@let
                    shareIt(out, list, 2, 3)
                    list.forEach { it.`in`.one = context.getString(full_sisters_2_3) }
                }

                /*
                 * Daughters of sons
                 */
                grandchildren.daughtersOfSons.thatEligible().let { list ->
                    if (children.daughters.thatEligible().isNotEmpty()) return@let
                    shareIt(out, list, 2, 3)
                    list.forEach { it.`in`.one = context.getString(daughters_of_sons_2_3) }
//                    TODO("How if sons is not empty?")
                }

                /*
                 * Full sisters
                 * Saudara perempuan seayah seibu mendapatkan 2/3 jika bersama dan kalalah
                 * Dibagi rata.
                 */
                siblings.fullSisters.thatEligible().let { list ->
                    if (!kalalah || list.size <= 1) return@let
                    shareIt(out, list, 2, 3)
                    list.forEach { it.`in`.one = context.getString(full_sisters_2_3) }
                }

                /*
                 * Paternal sisters
                 * Saudara perempuan seayah mendapatkan 2/3 jika bersama, kalalah, dan
                 * pewaris tidak mempunyai saudara perempuan seayah seibu.
                 * Dibagi rata.
                 */
                siblings.paternalSisters.thatEligible().let { list ->
                    if (!kalalah || list.size <= 1 || siblings.fullSisters.thatEligible().isNotEmpty()) return@let
                    shareIt(out, list, 2, 3)
                    list.forEach { it.`in`.one = context.getString(paternal_sisters_2_3) }
                }
            }

            /*
             * 1/3
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Dad
                 * Ayah mendapatkan 1/3 jika tidak terdapat anak
                 * An Nisa (4) : 11
                 */
                dad.takeIf { it.eligible }?.let {
                    if (children.children.thatEligibleIsExist()) return@let
                    shareIt(out, listOf(dad), 1, 3)
                    dad.`in`.one = context.getString(dad_1_3)
                }

                /*
                 * Mom
                 * Ibu mendapatkan 1/3 jika pewaris tidak mempunyai anak dan tidak mempunyai saudara
                 * An Nisa 4:11
                 */
                mom.takeIf { it.eligible }?.let {
                    if (children.children.isNotEmpty() || grandchildren.childrenOfSons.isNotEmpty() || siblings.fullSiblings.isNotEmpty()) return@let
                    shareIt(out, listOf(mom), 1, 3)
                    mom.`in`.one = context.getString(mom_1_3)
                }

                /*
                 * Maternal siblings
                 * Saudara seibu mendapatkan 1/3 jika bersama dan kalalah.
                 * Dibagi rata.
                 * QS An Nisa : 12
                 */
                siblings.maternalSiblings.thatEligible().let { list ->
                    if (!kalalah || list.size <= 1) return@let
                    shareIt(out, list, 1, 3)
                    list.forEach { it.`in`.one = context.getString(maternal_siblings_1_3) }
                }
            }

            /*
             * 1/6
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Dad
                 * Ayah mendapatkan 1/6 jika pewaris mempunyai anak
                 * QS An Nisa : 11
                 */
                dad.takeIf { it.eligible }?.let {
                    if (!children.children.thatEligibleIsExist()) return@let
                    shareIt(out, listOf(dad), 1, 6)
                    dad.`in`.one = context.getString(dad_1_6)
                }

                /*
                 * Mom
                 * Ibu mendapatkan 1/6 jika pewaris mempunyai anak atau mempunyai beberapa saudara
                 * QS An Nisa : 11
                 */
                mom.takeIf { it.eligible }?.let {
                    if (children.children.thatEligibleIsExist() || siblings.fullSiblings.thatEligibleIsMany()) {
                        shareIt(out, listOf(mom), 1, 6)
                        mom.`in`.one = context.getString(mom_1_6)
                    }
                }

                /*
                 * Dad of dad
                 * mendapatkan 1/6
                 * but might be disentitled by dad
                 */
                grandpas.dadOfDad.thatEligible().forEach {
                    if (dad.eligible) return@forEach
                    shareIt(out, listOf(dad), 1, 6)
                    it.`in`.one = context.getString(dad_of_dad_1_6)
                }

                /*
                 * Grandmas
                 */
                grandmas.grandmas.thatEligible().let { list ->
                    if (children.children.thatEligibleIsExist() || siblings.fullSiblings.thatEligibleIsMany())
                        shareIt(out, list, 1, 6)
                    list.forEach { it.`in`.one = context.getString(grandmas_1_6) }
                }

                /*
                 * Daughters of sons
                 */
                grandchildren.daughtersOfSons.thatEligible().let { list ->
                    if (children.daughters.size != 1) return@let
                    shareIt(out, list, 1, 6)
                    list.forEach { it.`in`.one = context.getString(daughter_of_son_1_6) }
                }

                /*
                 * Paternal sisters
                 */
                siblings.paternalSisters.thatEligible().let { list ->
                    if (siblings.fullSisters.size != 1 || list.size <= 1) return@let
                    shareIt(out, list, 1, 6)
                    list.forEach { it.`in`.one = context.getString(paternal_sisters_1_6) }
                }

                /*
                 * Maternal siblings
                 * Saudara seibu mendapatkan 1/6 jika sendiri dan kalalah.
                 * Dibagi rata.
                 * An Nisa 4:12
                 */
                siblings.maternalSiblings.thatEligible().let { list ->
                    if (!kalalah || list.size <= 1) return@let
                    shareIt(out, list, 1, 6)
                    list.forEach { it.`in`.one = context.getString(maternal_sibling_1_6) }
                }
            }
        }

        fun shareSpecial() {

            /*
             * When
             * inheritee leaves dad and spouse,
             * after assigning the respective shares due to them,
             * 1/3 of the Remainder will be assigned to the mother.
             */
            if (dad.eligible && (husband.thatEligibleIsExist() || wives.thatEligibleIsExist())) {
                mom.`in`.special = context.getString(mom_special)
                var spentToThem = dad.`in`.primary
                if (husband.thatEligibleIsExist()) spentToThem += husband[0].`in`.primary
                if (wives.thatEligibleIsExist()) wives.thatEligible().forEach {
                        spentToThem += it.`in`.primary
                    }

                ((deceased.legacy.shareable - spentToThem) / 3).let { toMom ->
                    mom.`in`.specialAmount += toMom
                    deceased.legacy.primaryShared += toMom
                    mom.`in`.special = context.getString(mom_special)
                }
            }
        }

        fun shareSecondary() {
            deceased.legacy.secondaryShareable.let shared@{ secondaryShareable ->

                /*
                 * Sons
                 * (and could be with daughters)
                 */
                children.sons.thatEligible().let { sons ->
                    if (sons.isEmpty()) return@let

                    /*
                     * Themselves
                     */
                    if (!children.daughters.thatEligibleIsExist()) {
                        sons.forEach {
                            it.`in`.secondary += secondaryShareable / sons.size
                            it.`in`.two = context.getString(son_secondary)
                        }
                    }

                    /*
                     * With daughters
                     */
                    else {
                        val totalSize = (sons.size * 2) + children.daughters.thatEligible().size
                        sons.forEach {
                            it.`in`.secondary += secondaryShareable / totalSize * 2
                            it.`in`.two = context.getString(son_secondary)
                        }
                        children.daughters.thatEligible().forEach {
                            it.`in`.secondary += secondaryShareable / totalSize
                            it.`in`.two = context.getString(daughter_secondary)
                        }

                    }


                    return@shared
                }

                /*
                 * Sons of sons
                 */
                grandchildren.sonsOfSons.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary += secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
//                TODO("Are they could be sharing with daughters or others?")
                }

                /*
                 * Dad
                 */
                dad.takeIf { it.eligible }?.let {
                    it.`in`.secondary += secondaryShareable
                    return@shared
                }

                /*
                 * Dad of dad
                 */
                grandpas.dadOfDad.thatEligible().forEach {
                    it.`in`.secondary = secondaryShareable
                    return@shared
                }

                /*
                 * Full brothers
                 */
                siblings.fullBrothers.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary += secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
//                TODO("Are they could be sharing with full sisters or others?")
                }

                /*
                 * Paternal brothers
                 */
                siblings.paternalBrothers.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
//                TODO("Are they could be sharing with full sisters or others?")
                }

                /*
                 * Sons of full brothers
                 */
                nephews.sonsOfFullBrothers.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
                }

                /*
                 * Sons of paternal brothers
                 */
                nephews.sonsOfPaternalBrothers.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
                }

                /*
                 * Full brothers of dad
                 */
                uncles.fullBrothersOfDad.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
                }

                /*
                 * Sons of full brothers of dad
                 */
                maleCousins.sonsOfFullBrothersOfDad.thatEligible().let { list ->
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                    if (list.isNotEmpty()) return@shared
                }
            }
        }

        fun resetCalculation() {
            deceased.resetShared()
            dad.resetIn()
            mom.resetIn()
            husband.forEach { it.resetIn() }
            wives.forEach { it.resetIn() }
            children.children.forEach { it.resetIn() }
            siblings.siblings.forEach { it.resetIn() }
            updateEligible()
        }

        resetCalculation()
        sharePrimary()
        shareSpecial()
        shareSecondary()
    }

    @Ignore
    fun setHeir(
        position: Position,
        order: Int,
        heir: Heir,
        context: Context
    ) {

        /*
         * Add
        */
        if (position in listOf(DAD, MOM) || order == -1) {
            when (position) {
                DAD -> dad = heir.apply { gender = MALE }
                MOM -> mom = heir.apply{ gender = FEMALE }
                HUSBAND -> husband.add(heir.apply { gender = MALE })
                WIFE -> wives.add(heir.apply { gender = FEMALE })
                CHILD -> children.children.add(heir)
                SIBLING -> siblings.siblings.add(heir as Sibling)
                GRANDPA -> grandpas.grandpas.add(heir.apply { gender = MALE } as Grandpa)
                GRANDMA -> grandmas.grandmas.add(heir.apply { gender = FEMALE } as Grandma)
                GRANDCHILD -> grandchildren.grandchildren.add(heir as Grandchild)
                UNCLE -> uncles.uncles.add(heir.apply { gender = MALE } as Uncle)
                MALE_COUSIN -> maleCousins.maleCousins.add(heir.apply { gender = MALE } as MaleCousin)
                NEPHEW -> nephews.nephews.add(heir.apply { gender = MALE } as Nephew)
                else -> 0.inc()
            }
        }

        /*
         * Edit
         */
        else {
            when (position) {
                DAD -> dad = heir.apply { gender = MALE }
                MOM -> mom = heir.apply{ gender = FEMALE }
                HUSBAND -> husband[order] = heir
                WIFE -> wives[order] = heir
                CHILD -> children.children[order] = heir
                SIBLING -> siblings.siblings[order] = heir as Sibling
                GRANDPA -> grandpas.grandpas[order] = heir as Grandpa
                GRANDMA -> grandmas.grandmas[order] = heir as Grandma
                GRANDCHILD -> grandchildren.grandchildren[order] = heir as Grandchild
                UNCLE -> uncles.uncles[order] = heir as Uncle
                MALE_COUSIN -> maleCousins.maleCousins[order] = heir as MaleCousin
                NEPHEW -> nephews.nephews[order] = heir as Nephew
                else -> 0.inc()
            }
        }

        calculate(context)
    }

    @Ignore
    fun getHeir(
        position: Position,
        order: Int
    ): Heir {

        return when (position) {
            DAD -> dad
            MOM -> mom
            HUSBAND -> husband[order]
            WIFE -> wives[order]
            CHILD -> children.children[order]
            SIBLING -> siblings.siblings[order]
            GRANDPA -> grandpas.grandpas[order]
            GRANDMA -> grandmas.grandmas[order]
            GRANDCHILD -> grandchildren.grandchildren[order]
            UNCLE -> uncles.uncles[order]
            MALE_COUSIN -> maleCousins.maleCousins[order]
            NEPHEW -> nephews.nephews[order]
            else -> Heir()
        }
    }

    @Ignore
    fun getHeirList(position: Position): List<Heir> {
        return when (position) {
            DAD -> listOf(dad)
            MOM -> listOf(mom)
            HUSBAND -> husband
            WIFE -> wives
            CHILD -> children.children
            SIBLING -> siblings.siblings
            GRANDPA -> grandpas.grandpas
            GRANDMA -> grandmas.grandmas
            GRANDCHILD -> grandchildren.grandchildren
            UNCLE -> uncles.uncles
            MALE_COUSIN -> maleCousins.maleCousins
            else -> nephews.nephews
        }
    }

    @Ignore
    fun deleteHeir(
        position: Position,
        order: Int
    ) {

        when (position) {
            DAD -> dad = Heir()
            MOM -> mom = Heir()
            HUSBAND -> husband.removeAt(order)
            WIFE -> wives.removeAt(order)
            CHILD -> children.children.removeAt(order)
            SIBLING -> siblings.siblings.removeAt(order)
            GRANDPA -> grandpas.grandpas.removeAt(order)
            GRANDMA -> grandmas.grandmas.removeAt(order)
            GRANDCHILD -> grandchildren.grandchildren.removeAt(order)
            UNCLE -> uncles.uncles.removeAt(order)
            MALE_COUSIN -> maleCousins.maleCousins.removeAt(order)
            NEPHEW -> nephews.nephews.removeAt(order)
            else -> 0.inc()
        }
    }
}