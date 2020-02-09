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

    /**
     * Kalalah
     * is a condition where the deceased didn't leave
     * son, son of son, dad, and dad of dad.
     */
    private val kalalah: Boolean
        get() {
            return !(dad.thatEligibleIsExist() || grandpas.dadOfDad.thatEligibleIsExist() || children.sons.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist())
        }

    private val spouses: List<Heir>
        get() {
            return husband + wives
        }

    @Embedded(prefix = "inheritee_")
    var deceased = Deceased()

    var dad = mutableListOf<Heir>()

    var mom = mutableListOf<Heir>()

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
                it.eligibleTwo = !children.sons.thatEligibleIsExist()
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_children_of_sons)
            }

            /**
             * Dad of dad
             * is disentitled by dad
             */
            grandpas.dadOfDad.forEach {
                it.eligibleTwo = !dad.thatEligibleIsExist()
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_dad_of_dad)
            }

            /*
             * Mom of dad
             */
            grandmas.momOfDad.forEach {
                it.eligibleTwo = !(dad.thatEligibleIsExist() || mom.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_mom_of_dad)
            }

            /*
             * Mom of mom
             */
            grandmas.momOfMom.forEach {
                it.eligibleTwo = !mom.thatEligibleIsExist()
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
                //                it.eligibleTwo = !(dad.thatEligibleIsExist() || children.sons.thatEligibleIsExist())
                it.eligibleTwo = kalalah
                eligibleBefore = !it.eligibleTwo
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
                //                it.eligibleTwo = !(eligibleBefore || siblings.fullBrothers.thatEligibleIsExist())
                it.eligibleTwo = !(!kalalah || siblings.fullBrothers.thatEligibleIsExist())
                if (!it.eligibleTwo) {
                    it.`in`.disentitler = context.getString(disentitled_paternal_brothers)
                }
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
                // it.eligibleTwo = !(dad.thatEligibleIsExist() || grandpas.dadOfDad.thatEligibleIsExist() || children.children.thatEligibleIsExist())
                it.eligibleTwo = kalalah
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_maternal_brothers)
            }

            /*
             * Paternal sisters
             */
            siblings.paternalSisters.forEach {
                //                it.eligibleTwo = !(children.sons.thatEligibleIsExist() || grandchildren.sonsOfSons.thatEligibleIsExist() || dad.thatEligibleIsExist() || siblings.fullBrothers.thatEligibleIsExist())
                it.eligibleTwo = kalalah
            }


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
                //                it.eligibleTwo = !(eligibleBefore || grandpas.dadOfDad.thatEligibleIsExist() || grandchildren.sonsOfSons.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist())
                it.eligibleTwo =
                    !(!kalalah || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist())
                eligibleBefore = !it.eligibleTwo
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
                //                it.eligibleTwo = !(eligibleBefore || nephews.sonsOfFullBrothers.thatEligibleIsExist())
                it.eligibleTwo =
                    !(!kalalah || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist() || nephews.sonsOfFullBrothers.thatEligibleIsExist())
                eligibleBefore = !it.eligibleTwo
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
                //                it.eligibleTwo = !(eligibleBefore || nephews.sonsOfPaternalBrothers.thatEligibleIsExist())
                it.eligibleTwo =
                    !(!kalalah || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist() || nephews.sonsOfFullBrothers.thatEligibleIsExist() || nephews.sonsOfPaternalBrothers.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_full_brothers_of_dad)
            }

            /*
             * Uncles : Paternal brothers of dad
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
                //                it.eligibleTwo = !(eligibleBefore || siblings.fullBrothers.thatEligibleIsExist())
                it.eligibleTwo =
                    !(!kalalah || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist() || nephews.sonsOfFullBrothers.thatEligibleIsExist() || nephews.sonsOfPaternalBrothers.thatEligibleIsExist() || uncles.fullBrothersOfDad.thatEligibleIsExist())
                eligibleBefore = !it.eligibleTwo
                if (!it.eligibleTwo)
                    it.`in`.disentitler = context.getString(disentitled_paternal_brothers_of_dad)
            }

            /*
             * Male Cousins : Sons of paternal brothers of dad
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
                //                it.eligibleTwo = !(eligibleBefore || uncles.paternalBrothersOfDad.thatEligibleIsExist())
                it.eligibleTwo =
                    !(!kalalah || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist() || nephews.sonsOfFullBrothers.thatEligibleIsExist() || nephews.sonsOfPaternalBrothers.thatEligibleIsExist() || uncles.fullBrothersOfDad.thatEligibleIsExist() || uncles.paternalBrothersOfDad.thatEligibleIsExist())
                if (!it.eligibleTwo)
                    it.`in`.disentitler =
                        context.getString(disentitled_sons_of_paternal_brothers_of_dad)
            }
        }

        fun sharePrimary() {

            fun shareIt(
                legacy: Legacy,
                list: List<Heir>,
                multiplier: Int,
                divider: Int
            ) {

                if (list.isEmpty()) return

                /*
                 * Record the amount of shared primary inheritance
                 */
                legacy.primaryShared += legacy.shareable / divider * multiplier
                Log.i("HEHEHE", "From: ${list[0].name}")
                Log.i("HEHEHE", "Just shared: ${legacy.shareable / divider * multiplier}")

                /*
                 * Share the inheritance evenly
                 */
                list.forEach {
                    it.`in`.primary += legacy.shareable / divider / list.size * multiplier
                }
            }

            fun explainIt(
                list: List<Heir>,
                text: Int
            ) {
                list.forEach {
                    it.`in`.one = context.getString(text)
                }
            }

            /*
             * 1/2
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Husband
                 * gets 1/2 if the deceased didn't left child and child of son
                 * QS An Nisa : 12
                 */
                husband.thatEligible().let { husband ->
                    if (husband.isEmpty()) return@let
                    if (children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist()) return@let
                    shareIt(out, husband, 1, 2)
                    explainIt(husband, husband_1_2)
                }

                /*
                 * Daughter
                 * get 1/2 if alone and the deceased didn't leave son
                 * QS An Nisa : 11
                 * KHI : 176
                 */
                children.daughters.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (children.sons.isNotEmpty() || list.size > 1) return@let
                    shareIt(out, list, 1, 2)
                    explainIt(list, daughter_1_2)
                }

                /*
                 * Daughter of son
                 * get 1/2 if alone and the deceased didn't leave
                 * daughter and son of son
                 */
                grandchildren.daughtersOfSons.thatEligible().let { list ->
                    if (list.isEmpty()) return@let

                    /*
                     * Violate the prerequisite
                     */
                    if (children.daughters.thatEligibleIsExist() || grandchildren.sonsOfSons.thatEligibleIsExist() || list.size > 1) return@let
                    shareIt(out, list, 1, 2)
                    explainIt(list, daughter_of_son_1_2)
                }

                /*
                 * Full sister
                 * get 1/2 if alone, kalalah, and
                 * the deceased didn't leave full brother, daughter, and daughter of son
                 */
                siblings.fullSisters.thatEligible().let { list ->

                    /*
                     * Violate the prerequisite
                     */
                    if (!kalalah || list.size > 1 || siblings.fullBrothers.thatEligibleIsExist() || children.daughters.thatEligibleIsExist() || grandchildren.daughtersOfSons.thatEligibleIsExist()) return@let

                    shareIt(out, list, 1, 2)
                    explainIt(list, full_sister_1_2)
                }

                /*
                 * Paternal sisters
                 * get 1/2 if alone, kalalah,
                 * and the deceased didn't leave paternal brothers.
                 * daughter, daughter of son,
                 * full sister, and full brother.
                 */
                siblings.paternalSisters.thatEligible().let { list ->
                    if (list.size > 1 || !kalalah || children.daughters.thatEligibleIsExist() || grandchildren.daughtersOfSons.thatEligibleIsExist() || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist() || siblings.fullSisters.thatEligibleIsExist()) return@let
                    shareIt(out, list, 1, 2)
                    explainIt(list, paternal_sister_1_2)
                }
            }

            /*
             * 1/4
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Husband
                 * gets 1/4 if the deceased left child or child of son
                 * An Nisa (4) : 12
                 */
                husband.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (!(children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist())) return@let
                    shareIt(out, list, 1, 4)
                    explainIt(list, husband_1_4)
                }

                /*
                 * Wives
                 * get 1/4 if the deceased didn't leave child and child of son
                 * Divided equally
                 * QS An Nisa : 12
                 */
                wives.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist()) return@let
                    shareIt(out, list, 1, 4)
                    explainIt(list, wives_1_4)
                }
            }

            /*
             * 1/8
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Wives
                 * get 1/8 if the deceased left child or child of son
                 * Divided equally.
                 * QS An Nisa : 12
                 */
                wives.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (!(children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist())) return@let
                    shareIt(out, list, 1, 8)
                    explainIt(list, wives_1_8)
                }
            }

            /*
             * 2/3
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Daughters
                 * get 2/3 if together and the deceased didn't leave son.
                 * Divided equally.
                 * An Nisa 4 : 11
                 */
                children.daughters.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (children.sons.thatEligibleIsExist() || list.size <= 1) return@let
                    shareIt(out, list, 2, 3)
                    explainIt(list, daughters_2_3)
                }

                /*
                 * Daughters of sons
                 * get 2/3 if together and the deceased didn't leave
                 * daughter and son of son.
                 * Divided equally.
                 */
                grandchildren.daughtersOfSons.thatEligible().let { list ->
                    if (list.isEmpty()) return@let

                    /*
                     * Violate the prerequisite
                     */
                    if (children.daughters.thatEligibleIsExist() || list.size < 2 || grandchildren.sonsOfSons.thatEligibleIsExist()) return@let

                    shareIt(out, list, 2, 3)
                    explainIt(list, daughters_of_sons_2_3)
                }

                /*
                 * Full sisters
                 * get 2/3 if together, kalalah, and
                 * the deceased didn't leave full brother, daughter, and daughter of son
                 * Divided equally.
                 */
                siblings.fullSisters.thatEligible().let { list ->
                    if (list.isEmpty()) return@let

                    /*
                     * Violate the prerequisite
                     */
                    if (!kalalah || list.size < 2 || siblings.fullBrothers.thatEligibleIsExist() || children.daughters.thatEligibleIsExist() || grandchildren.daughtersOfSons.thatEligibleIsExist()) return@let

                    shareIt(out, list, 2, 3)
                    explainIt(list, full_sisters_2_3)
                }

                /*
                 * Paternal sisters
                 * get 1/2 if together, kalalah,
                 * and the deceased didn't leave paternal brothers.
                 * daughter, daughter of son,
                 * full sister, and full brother.
                 */
                siblings.paternalSisters.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (list.size <= 1 || !kalalah || children.daughters.thatEligibleIsExist() || grandchildren.daughtersOfSons.thatEligibleIsExist() || siblings.fullBrothers.thatEligibleIsExist() || siblings.fullSisters.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist()) return@let
                    shareIt(out, list, 2, 3)
                    explainIt(list, paternal_sisters_2_3)
                }
            }

            /*
             * 1/3
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Mom
                 * get 1/3 if the deceased didn't leave child, child of son,
                 * number of full sibling is < 2
                 * An Nisa 4:11
                 */
                mom.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist() || siblings.fullSiblings.thatEligibleIsMany()) return@let
                    shareIt(out, list, 1, 3)
                    explainIt(list, mom_1_3)
                }

                /*
                 * Maternal siblings
                 * get 1/3 if together and kalalah.
                 * Divided equally.
                 *
                 * QS An Nisa : 12
                 */
                siblings.maternalSiblings.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (!kalalah || list.size <= 1) return@let
                    shareIt(out, list, 1, 3)
                    explainIt(list, maternal_siblings_1_3)
              }
            }

            /*
             * 1/6
             */
            deceased.legacy.let out@{ out ->

                /*
                 * Dad
                 * gets 1/6 if the deceased left child or child of son
                 * QS An Nisa : 11
                 */
                dad.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (!(children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist())) return@let
                    shareIt(out, list, 1, 6)
                    explainIt(list, dad_1_6)
                }

                /*
                 * Mom
                 * gets 1/6 if the deceased left child
                 * or child of son
                 * or number of full siblings is > 1
                 * QS An Nisa : 11
                 */
                mom.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist() || siblings.fullSiblings.thatEligibleIsMany()) {
                        shareIt(out, list, 1, 6)
                        explainIt(list, mom_1_6)
                    }
                }

                /*
                 * Dad of dad
                 * gets 1/6 if the deceased left child or child of son
                 */
                grandpas.dadOfDad.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (!(children.children.thatEligibleIsExist() || grandchildren.childrenOfSons.thatEligibleIsExist())) return@let
                    shareIt(out, list, 1, 6)
                    explainIt(list, dad_of_dad_1_6)
                }

                /*
                 * Grandmas
                 * gets 1/6 if the deceased left child
                 * or number of full siblings is > 1
                 */
                grandmas.grandmas.thatEligible().let { list ->
                    if (children.children.thatEligibleIsExist() || siblings.fullSiblings.thatEligibleIsMany()) {
                        shareIt(out, list, 1, 6)
                        explainIt(list, grandmas_1_6)
                    }
                }

                /*
                 * Daughters of sons
                 * get 1/6 if the deceased left one daughter and
                 * didn't leave son of son
                 */
                grandchildren.daughtersOfSons.thatEligible().let { list ->
                    if (list.isEmpty()) return@let

                    if (children.daughters.thatEligible().size != 1 || grandchildren.sonsOfSons.thatEligibleIsExist()) return@let
                    shareIt(out, list, 1, 6)
                    explainIt(list, daughter_of_son_1_6)
                }

                /*
                 * Full sisters
                 * get 1/6 if alone, kalalah,
                 * the deceased didn't leave full brother,
                 * but the deceased left daughter or daughter of son
                 * Divided equally.
                 */
//                siblings.fullSisters.thatEligible().let { list ->
//                    if (list.isEmpty()) return@let
//
//                    /*
//                     * Violate the prerequisite
//                     */
//                    if (!kalalah || list.size > 1 || siblings.fullBrothers.thatEligibleIsExist() || !children.daughters.thatEligibleIsExist() || !grandchildren.daughtersOfSons.thatEligibleIsExist()) return@let
//
//                    shareIt(out, list, 1, 6)
//                    explainIt(list, full)
//                }

                /*
                 * Paternal sisters
                 * get 1/2 if alone, kalalah,
                 * the deceased left a full sister,
                 * and the deceased didn't leave paternal brothers.
                 * daughter, daughter of son,
                 * and full brother.
                 */
                siblings.paternalSisters.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (list.size <= 1 || !kalalah || siblings.fullSisters.thatEligible().size != 1 || children.daughters.thatEligibleIsExist() || grandchildren.daughtersOfSons.thatEligibleIsExist() || siblings.fullBrothers.thatEligibleIsExist() || siblings.paternalBrothers.thatEligibleIsExist()) return@let
                    shareIt(out, list, 1, 6)
                    explainIt(list, paternal_sisters_1_6)
                }

                /*
                 * Maternal siblings
                 * get 1/6 if alone and kalalah.
                 * Divided equally.
                 * An Nisa 4 : 12
                 */
                siblings.maternalSiblings.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    if (!kalalah || list.size > 1) return@let
                    shareIt(out, list, 1, 6)
                    explainIt(list, maternal_sibling_1_6)
                }
            }
        }

        fun shareSpecial() {

            /*
             * When
             * the deceased left mom, dad and spouse(s),
             * but didn't leave child and child of son,
             * after assigning the prescribed shares to spouse(s),
             * 1/3 of the remainder will be assigned to mom.
             */
            if (mom.thatEligibleIsExist() && dad.thatEligibleIsExist() && spouses.thatEligibleIsExist()) {

                /*
                 * Reset mom's in
                 */
                deceased.legacy.primaryShared -= mom[0].`in`.primary
                mom[0].`in`.primary = 0.0
                mom[0].`in`.one = ""

                var spentToThem = 0.0
                if (husband.thatEligibleIsExist()) spentToThem += husband[0].`in`.primary
                if (wives.thatEligibleIsExist()) wives.thatEligible().forEach {
                    spentToThem += it.`in`.primary
                }

                val toMom = (deceased.legacy.shareable - spentToThem) / 3
                mom[0].`in`.specialAmount += toMom
                deceased.legacy.primaryShared += toMom
                mom[0].`in`.special = context.getString(mom_special)

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
                 * (and could be with daughters of sons)
                 */
                grandchildren.sonsOfSons.thatEligible().let { list ->
                    if (list.isEmpty()) return@let

                    /*
                     * Themeselves
                     */
                    if (!grandchildren.daughtersOfSons.thatEligibleIsExist()) {
                        list.forEach {
                            it.`in`.secondary += secondaryShareable / list.size
                            it.`in`.two = context.getString(sons_of_sons_secondary)
                        }
                    }

                    /*
                     * With daughters of sons
                     */
                    else {
                        val totalSize =
                            (list.size * 2) + grandchildren.daughtersOfSons.thatEligible().size
                        list.forEach {
                            it.`in`.secondary += secondaryShareable / totalSize * 2
                            it.`in`.two = context.getString(sons_of_sons_secondary)
                        }
                        grandchildren.daughtersOfSons.thatEligible().forEach {
                            it.`in`.secondary += secondaryShareable / totalSize
                            it.`in`.two = context.getString(daughters_of_sons_secondary)
                        }
                    }
                }

                /*
                 * Dad
                 */
                dad.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    list.forEach {
                        it.`in`.secondary = secondaryShareable
                        it.`in`.two = context.getString(dad_secondary)
                    }
                }

                /*
                 * Dad of dad
                 */
                grandpas.dadOfDad.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    list.forEach {
                        it.`in`.secondary = secondaryShareable
                    }
                }

                /*
                 * Full brothers
                 * (and could be with full sisters)
                 */
                siblings.fullBrothers.thatEligible().let { list ->
                    val totalSize = (list.size * 2) + siblings.fullSisters.thatEligible().size

                    /*
                     * Themeselves
                     */
                    if (!siblings.fullSisters.thatEligibleIsExist()) {
                        list.forEach {
                            it.`in`.secondary += secondaryShareable / list.size
                            it.`in`.two = context.getString(full_brothers_secondary)
                        }
                    }

                    /*
                     * With full sisters
                      */
                    else {
                        list.forEach {
                            it.`in`.secondary += secondaryShareable / totalSize * 2
                            it.`in`.two = context.getString(full_brothers_secondary)
                        }
                        siblings.fullSisters.thatEligible().forEach {
                            it.`in`.secondary += secondaryShareable / totalSize
                            it.`in`.two = context.getString(full_sisters_secondary)
                        }
                    }

                    if (totalSize > 0) return@shared
                }

                /*
                 * Paternal brothers
                 * (and could be with paternal sister)
                 */
                siblings.paternalBrothers.thatEligible().let { list ->
                    val totalSize = (list.size * 2) + siblings.paternalSisters.thatEligible().size

                    /*
                     * Themeselves
                     */
                    if (!siblings.paternalSisters.thatEligibleIsExist()) {
                        list.forEach {
                            it.`in`.secondary += secondaryShareable / list.size
                            it.`in`.two = context.getString(paternal_brothers_secondary)
                        }
                    }

                    /*
                     * With paternal sisters
                      */
                    else {
                        list.forEach {
                            it.`in`.secondary += secondaryShareable / totalSize * 2
                            it.`in`.two = context.getString(paternal_brothers_secondary)
                        }
                        siblings.paternalSisters.thatEligible().forEach {
                            it.`in`.secondary += secondaryShareable / totalSize
                            it.`in`.two = context.getString(paternal_sisters_secondary)
                        }
                    }

                    if (totalSize > 0) return@shared
                }

                /*
                 * Sons of full brothers
                 */
                nephews.sonsOfFullBrothers.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                }

                /*
                 * Sons of paternal brothers
                 */
                nephews.sonsOfPaternalBrothers.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                }

                /*
                 * Full brothers of dad
                 */
                uncles.fullBrothersOfDad.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                }

                /*
                 * Sons of full brothers of dad
                 */
                maleCousins.sonsOfFullBrothersOfDad.thatEligible().let { list ->
                    if (list.isEmpty()) return@let
                    list.forEach {
                        it.`in`.secondary = secondaryShareable / list.size
                    }
                }
            }
        }

        fun resetCalculation() {
            deceased.resetShared()
            dad.forEach { it.resetIn() }
            mom.forEach { it.resetIn() }
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
        if (order == -1) {
            when (position) {
                DAD -> dad.add(heir.apply { gender = MALE })
                MOM -> mom.add(heir.apply { gender = FEMALE })
                HUSBAND -> husband.add(heir.apply { gender = MALE })
                WIFE -> wives.add(heir.apply { gender = FEMALE })
                CHILD -> children.children.add(heir)
                SIBLING -> siblings.siblings.add(heir as Sibling)
                GRANDPA -> grandpas.grandpas.add(heir.apply { gender = MALE } as Grandpa)
                GRANDMA -> grandmas.grandmas.add(heir.apply { gender = FEMALE } as Grandma)
                GRANDCHILD -> grandchildren.grandchildren.add(heir as Grandchild)
                UNCLE -> uncles.uncles.add(heir.apply { gender = MALE } as Uncle)
                MALE_COUSIN -> maleCousins.maleCousins.add(heir.apply {
                    gender = MALE
                } as MaleCousin)
                NEPHEW -> nephews.nephews.add(heir.apply { gender = MALE } as Nephew)
                else -> 0.inc()
            }
        }

        /*
         * Edit
         */
        else {
            when (position) {
                DAD -> dad.add(heir.apply { gender = MALE })
                MOM -> mom.add(heir.apply { gender = FEMALE })
                HUSBAND -> husband[order] = heir.apply { gender = MALE }
                WIFE -> wives[order] = heir.apply { gender = FEMALE }
                CHILD -> children.children[order] = heir
                SIBLING -> siblings.siblings[order] = heir as Sibling
                GRANDPA -> grandpas.grandpas[order] = heir.apply { gender = MALE } as Grandpa
                GRANDMA -> grandmas.grandmas[order] = heir.apply { gender = FEMALE } as Grandma
                GRANDCHILD -> grandchildren.grandchildren[order] = heir as Grandchild
                UNCLE -> uncles.uncles[order] = heir.apply { gender = MALE } as Uncle
                MALE_COUSIN -> maleCousins.maleCousins[order] = heir.apply { gender = MALE } as MaleCousin
                NEPHEW -> nephews.nephews[order] = heir.apply { gender = MALE } as Nephew
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
            DAD -> dad[order]
            MOM -> dad[order]
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
            DAD -> dad
            MOM -> mom
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
            DAD -> dad.removeAt(order)
            MOM -> mom.removeAt(order)
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
